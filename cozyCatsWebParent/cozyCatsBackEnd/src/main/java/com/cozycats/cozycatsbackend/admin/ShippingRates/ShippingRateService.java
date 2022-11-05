package com.cozycats.cozycatsbackend.admin.ShippingRates;

import Exceptions.ShippingRateAlreadyExistsException;
import Exceptions.ShippingRateNotFoundException;
import com.cozycats.cozycatsbackend.admin.Product.ProductRepository;
import com.cozycats.cozycatsbackend.admin.Setting.Country.CountryRepository;
import com.cozycats.cozycatscommon.entity.Country;
import com.cozycats.cozycatscommon.entity.Product;
import com.cozycats.cozycatscommon.entity.ShippingRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ShippingRateService {
    public static final int RATES_PER_PAGE = 10;
    private static final int DIM_DIVISOR = 139;
    @Autowired
    private ShippingRateRepository shipRepo;
    @Autowired
    private CountryRepository countryRepo;
    @Autowired
    private ProductRepository productRepo;

    public List<ShippingRate> listAll() {
        return (List<ShippingRate>) shipRepo.findAll();
    }

    public Page<ShippingRate> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, RATES_PER_PAGE, sort);

        if (keyword != null) {
            return shipRepo.findAll(keyword, pageable);
        }

        return shipRepo.findAll(pageable);
    }

    public List<Country> listAllCountries() {
        return countryRepo.findAllByOrderByNameAsc();
    }

    public void save(ShippingRate rateInForm) throws ShippingRateAlreadyExistsException {
        ShippingRate rateInDB = shipRepo.findByCountryAndState(
                rateInForm.getCountry().getId(), rateInForm.getState());
        boolean foundExistingRateInNewMode = rateInForm.getId() == null && rateInDB != null;
        boolean foundDifferentExistingRateInEditMode = rateInForm.getId() != null && rateInDB != null && !rateInDB.equals(rateInForm);

        if (foundExistingRateInNewMode || foundDifferentExistingRateInEditMode) {
            throw new ShippingRateAlreadyExistsException("There's already a rate for the destination "
                    + rateInForm.getCountry().getName() + ", " + rateInForm.getState());
        }
        shipRepo.save(rateInForm);
    }

    public ShippingRate get(Integer id) throws ShippingRateNotFoundException {
        try {
            return shipRepo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
        }
    }

    public void updateCODSupport(Integer id, boolean codSupported) throws ShippingRateNotFoundException {
        Long count = shipRepo.countById(id);
        if (count == null || count == 0) {
            throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
        }

        shipRepo.updateCODSupport(id, codSupported);
    }

    public void delete(Integer id) throws ShippingRateNotFoundException {
        Long count = shipRepo.countById(id);
        if (count == null || count == 0) {
            throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);

        }
        shipRepo.deleteById(id);
    }

    public float calculateShippingCost(Integer productId, Integer countryId, String state)
            throws ShippingRateNotFoundException {
        ShippingRate shippingRate = shipRepo.findByCountryAndState(countryId, state);

        if (shippingRate == null) {
            throw new ShippingRateNotFoundException("No shipping rate found for the given "
                    + "destination. You have to enter shipping cost manually.");
        }

        Product product = productRepo.findById(productId).get();

        float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
        float finalWeight = product.getWeigth() > dimWeight ? product.getWeigth() : dimWeight;

        return finalWeight * shippingRate.getRate();
    }
}
