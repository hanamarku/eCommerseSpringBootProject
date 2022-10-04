package com.cozycats.cozycatsbackend.admin.Setting.State;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import com.cozycats.cozycatsbackend.admin.Setting.Country.CountryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.cozycats.cozycatscommon.entity.Country;
import com.cozycats.cozycatscommon.entity.State;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class StateRepositoryTests {

	@Autowired private StateRepository repo;
	@Autowired private CountryRepository countryRepository;
	
	@Test
	public void testCreateStatesInIndia() {
		Integer countryId = 3;
		Country country = countryRepository.findById(countryId).get();

		State state1 = repo.save(new State("Durres", country));
		State state2 = repo.save(new State("Elbasan", country));
		State state3 = repo.save(new State("Kukes", country));

		assertThat(state1).isNotNull();
		assertThat(state1.getId()).isGreaterThan(0);
		assertThat(state2).isNotNull();
		assertThat(state2.getId()).isGreaterThan(0);
		assertThat(state3).isNotNull();
		assertThat(state3.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateStatesInAl() {
		Integer countryId = 3;
		Country country = countryRepository.findById(countryId).get();
		State state = repo.save(new State("Tirane", country));
		
		assertThat(state).isNotNull();
		assertThat(state.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListStatesByCountry() {
		Integer countryId = 2;
		Country country = countryRepository.findById(countryId).get();
		List<State> listStates = repo.findByCountryOrderByNameAsc(country);
		
		listStates.forEach(System.out::println);
		
		assertThat(listStates.size()).isGreaterThan(0);
	}
	
	@Test
	public void testUpdateState() {
		Integer stateId = 3;
		String stateName = "Diber";
		State state = repo.findById(stateId).get();
		
		state.setName(stateName);
		State updatedState = repo.save(state);
		
		assertThat(updatedState.getName()).isEqualTo(stateName);
	}
	
	@Test
	public void testGetState() {
		Integer stateId = 1;
		Optional<State> findById = repo.findById(stateId);
		assertThat(findById.isPresent());
	}
	
	@Test
	public void testDeleteState() {
		Integer stateId = 7;
		repo.deleteById(stateId);

		Optional<State> findById = repo.findById(stateId);
		assertThat(findById.isEmpty());		
	}
}
