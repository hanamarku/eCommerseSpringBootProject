package com.cozycats.cozycatsbackend.admin.Setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.cozycats.cozycatsbackend.admin.Currency.CurrencyRepository;
import com.cozycats.cozycatscommon.entity.Brand;
import com.cozycats.cozycatscommon.entity.Currency;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;



@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CurrencyRepositoryTests {

	@Autowired
	private CurrencyRepository repo;
	
	@Test
	public void testCreateCurrencies() {
//		List<Currency> listCurrencies = Arrays.asList(
//			new Currency("United States Dollar", "$", "USD"),
//			new Currency("British Pound", "£", "GPB"),
//			new Currency("Japanese Yen", "¥", "JPY"),
//			new Currency("Euro", "€", "EUR"),
//			new Currency("Russian Ruble", "₽", "RUB"),
//			new Currency("South Korean Won", "₩", "KRW"),
//			new Currency("Chinese Yuan", "¥", "CNY"),
//			new Currency("Brazilian Real", "R$", "BRL"),
//			new Currency("Australian Dollar", "$", "AUD"),
//			new Currency("Canadian Dollar", "$", "CAD"),
//			new Currency("Vietnamese đồng ", "₫", "VND"),
//			new Currency("Indian Rupee", "₹", "INR")

//		);
//		Currency c1 = new Currency("Albanian lek", "L", "ALL");
		Integer id = 13;
		repo.deleteById(id);

		Optional<Currency> result = repo.findById(id);

		AssertionsForClassTypes.assertThat(result.isEmpty());
//		repo.saveAll(listCurrencies);
//		repo.save(c1);
		
//		Iterable<Currency> iterable = repo.findAll();
//		assertThat(iterable).size().isEqualTo(12);
//		assertThat(c1).isNotNull();
	}

	@Test
	public void testListAllOrderByNameAsc() {
		List<Currency> currencies = repo.findAllByOrderByNameAsc();

		currencies.forEach(System.out::println);

		assertThat(currencies.size()).isGreaterThan(0);
	}
}
