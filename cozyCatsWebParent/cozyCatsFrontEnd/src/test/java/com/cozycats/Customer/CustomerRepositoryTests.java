package com.cozycats.Customer;

import com.cozycats.cozycatscommon.entity.AuthenticationType;
import com.cozycats.cozycatscommon.entity.Country;
import com.cozycats.cozycatscommon.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {
    @Autowired
    private CustomerRepository repo;
    @Autowired private TestEntityManager entityManager;

    @Test
    public void testCreateCustomer1() {
        Integer countryId = 9;
        Country country = entityManager.find(Country.class, countryId);
        System.out.println(country);
        Customer customer = new Customer();
        customer.setCountry(country);
        customer.setFirstName("merita");
        customer.setLastName("lena");
        customer.setPassword("meri2020");
        customer.setEmail("testhhospital@gmail.com");
        customer.setPhoneNumber("0694298902");
        customer.setAddressLine1("1927  adress1");
        customer.setCity("Naju-si");
        customer.setState("Jeju");
        customer.setPostalCode("95867");
        customer.setCreatedTime(new Date());

        Customer savedCustomer = repo.save(customer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateCustomer2() {
        Integer countryId = 9;
        Country country = entityManager.find(Country.class, countryId);

        Customer customer = new Customer();
        customer.setCountry(country);
        customer.setFirstName("miiii");
        customer.setLastName("gjana");
        customer.setPassword("mira2020");
        customer.setEmail("mirjetaaa.marku@gmail.com");
        customer.setPhoneNumber("02224928052");
        customer.setAddressLine1("173 , A-, franko-gjini");
        customer.setAddressLine2("frnako gjini 2");
        customer.setCity("Sokcho-si");
        customer.setState("Korea");
        customer.setPostalCode("400013");
        customer.setCreatedTime(new Date());

        Customer savedCustomer = repo.save(customer);

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isGreaterThan(0);
    }

    @Test
    public void testListCustomers() {
        Iterable<Customer> customers = repo.findAll();
        customers.forEach(System.out::println);
        assertThat(customers).isNotNull();
    }

    @Test
    public void testUpdateCustomer() {
        Integer customerId = 2;
        String lastName = "Gjana";

        Customer customer = repo.findById(customerId).get();
        customer.setLastName(lastName);
        customer.setEnabled(true);

        Customer updatedCustomer = repo.save(customer);
        assertThat(updatedCustomer.getLastName()).isEqualTo(lastName);
    }

    @Test
    public void testGetCustomer() {
        Integer customerId = 2;
        Optional<Customer> findById = repo.findById(customerId);

        assertThat(findById).isPresent();

        Customer customer = findById.get();
        System.out.println(customer);
    }

    @Test
    public void testDeleteCustomer() {
        Integer customerId = 4;
        repo.deleteById(customerId);

        Optional<Customer> findById = repo.findById(customerId);
        assertThat(findById).isNotPresent();
    }

    @Test
    public void testFindByEmail() {
        String email = "mirjeta.marku@gmail.com";
        Customer customer = repo.findByEmail(email);

        assertThat(customer).isNotNull();
        System.out.println(customer);
    }

    @Test
    public void testFindByVerificationCode() {
        String code = "code_123";
        Customer customer = repo.findByVerificationCode(code);

        assertThat(customer).isNotNull();
        System.out.println(customer);
    }

    @Test
    public void testEnableCustomer() {
        Integer customerId = 1;
        repo.enable(customerId);

        Customer customer = repo.findById(customerId).get();
        assertThat(customer.isEnabled()).isTrue();
    }

    @Test
    public void testUpdateAuthType(){
        Integer id = 1;
        repo.updateAuthenticationType(id, AuthenticationType.FACEBOOK);
        Customer customer = repo.findById(id).get();
        assertThat(customer.getAuthenticationType()).isEqualTo(AuthenticationType.FACEBOOK);
    }
}
















