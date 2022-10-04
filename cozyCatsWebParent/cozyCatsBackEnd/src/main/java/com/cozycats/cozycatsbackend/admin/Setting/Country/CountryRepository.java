package com.cozycats.cozycatsbackend.admin.Setting.Country;

import com.cozycats.cozycatscommon.entity.Country;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CountryRepository extends CrudRepository<Country, Integer> {
    public List<Country> findAllByOrderByNameAsc();


}
