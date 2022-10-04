package com.cozycats.cozycatsbackend.admin.Currency;

import com.cozycats.cozycatscommon.entity.Currency;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CurrencyRepository extends CrudRepository<Currency, Integer> {

    public List<Currency> findAllByOrderByNameAsc();
}
