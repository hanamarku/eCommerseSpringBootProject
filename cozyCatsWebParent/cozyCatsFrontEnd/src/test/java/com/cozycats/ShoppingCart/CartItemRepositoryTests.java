package com.cozycats.ShoppingCart;

import com.cozycats.cozycatscommon.entity.CartItem;
import com.cozycats.cozycatscommon.entity.Customer;
import com.cozycats.cozycatscommon.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)

public class CartItemRepositoryTests {

    @Autowired
    private CartItemRepository repo;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testSaveItems(){
        Integer customerId = 1;
        Integer productId = 5;

        Customer customer = entityManager.find(Customer.class, customerId);
        Product product = entityManager.find(Product.class, productId);

        CartItem newItem = new CartItem();
        newItem.setCustomer(customer);
        newItem.setProduct(product);
        newItem.setQuantity(1);
        CartItem savedItem = repo.save(newItem);
        assertThat(savedItem.getId()).isGreaterThan(0);
    }

    @Test
    public void testSaveItems2(){
        Integer customerId = 2;
        Integer productId = 9;

        Customer customer = entityManager.find(Customer.class, customerId);
        Product product = entityManager.find(Product.class, productId);

        CartItem newItem = new CartItem();
        newItem.setCustomer(customer);
        newItem.setProduct(product);
        newItem.setQuantity(1);
        CartItem savedItem = repo.save(newItem);
        assertThat(savedItem.getId()).isGreaterThan(0);
    }
    
    @Test
    public void findByCustomerTest(){
        Integer customerId = 1;
        List<CartItem> listItems = repo.findByCustomer(new Customer(customerId));
        listItems.forEach(System.out::println);
        assertThat(listItems.size()).isEqualTo(1);
    }

    @Test
    public void findBuCustomerAndProductTest(){
        Integer customerId = 1;
        Integer productId = 5;

        CartItem byCustomerAndProduct = repo.findByCustomerAndProduct(new Customer(customerId), new Product(productId));
        assertThat(byCustomerAndProduct).isNotNull();
        System.out.println(byCustomerAndProduct);
    }

    @Test
    public void testUpdateQuantity(){
        Integer customerId = 1;
        Integer productId = 5;
        Integer quantity = 4;

        repo.updateQuantity(quantity, customerId, productId);
        CartItem item = repo.findByCustomerAndProduct(new Customer(customerId), new Product(productId));
        assertThat(item.getQuantity()).isEqualTo(4);
    }

    @Test
    public void deleteByCstomerAndProductTest(){
        Integer customerId = 2;
        Integer productId = 5;
        repo.deleteByCustomerAndProduct(customerId, productId);
        CartItem item = repo.findByCustomerAndProduct(new Customer(customerId), new Product(productId));
        assertThat(item).isNull();
    }
}
