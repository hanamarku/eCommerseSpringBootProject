package com.cozycats.cozycatsbackend.admin.Customer;

import com.cozycats.cozycatscommon.entity.Brand;
import com.cozycats.cozycatscommon.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {
    @Autowired
    private CustomerRepository repo;


    @Test
    public void testFindAll() {
        Iterable<Customer> customers = repo.findAll();
        customers.forEach(System.out::println);
    }
}
