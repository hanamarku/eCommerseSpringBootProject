package com.cozycats.Product;

import com.cozycats.cozycatscommon.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {
    @Autowired
    ProductRepository repo;

    @Test
    public void testFindByAlias(){
        String alias = "Cat Beds";
        Product product = repo.findByAlias(alias);
        assertThat(product).isNotNull();
    }
}
