package com.cozycats.Shipping;

import com.cozycats.cozycatscommon.entity.Country;
import com.cozycats.cozycatscommon.entity.ShippingRate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class ShippingRatesTest {

    @Autowired
    private ShippingRateRepository repo;

    @Test
    public void testFindByCountryAndState(){
        Country korea = new Country(9);
        String state = "North Chungcheong";
        ShippingRate byCountryAndState = repo.findByCountryAndState(korea, state);

        assertThat(byCountryAndState).isNotNull();
        System.out.println(byCountryAndState);
    }
}
