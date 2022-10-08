package com.cozycats.Setting;

import com.cozycats.cozycatscommon.entity.Country;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CountryRepository extends CrudRepository<Country, Integer> {
    public List<Country> findAllByOrderByNameAsc();

    @Query("select c from Country c where c.code = ?1")
    public Country findByCode(String code);


}
