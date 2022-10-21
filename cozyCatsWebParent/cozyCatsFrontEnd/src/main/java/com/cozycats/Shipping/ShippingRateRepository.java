package com.cozycats.Shipping;

import com.cozycats.cozycatscommon.entity.Country;
import com.cozycats.cozycatscommon.entity.ShippingRate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingRateRepository extends CrudRepository<ShippingRate, Integer> {

    public ShippingRate findByCountryAndState(Country country, String state);
}
