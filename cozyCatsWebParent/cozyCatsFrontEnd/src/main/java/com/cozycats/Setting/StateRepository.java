package com.cozycats.Setting;

import java.util.List;

import com.cozycats.cozycatscommon.entity.Country;
import com.cozycats.cozycatscommon.entity.State;
import org.springframework.data.repository.CrudRepository;


public interface StateRepository extends CrudRepository<State, Integer> {
	
	public List<State> findByCountryOrderByNameAsc(Country country);
}
