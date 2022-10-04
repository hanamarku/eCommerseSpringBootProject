package com.cozycats.cozycatsbackend.admin.Setting.State;

import com.cozycats.cozycatscommon.entity.Country;
import com.cozycats.cozycatscommon.entity.State;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StateRepository extends CrudRepository<State, Integer> {
    public List<State> findByCountryOrderByNameAsc(Country country);
}
