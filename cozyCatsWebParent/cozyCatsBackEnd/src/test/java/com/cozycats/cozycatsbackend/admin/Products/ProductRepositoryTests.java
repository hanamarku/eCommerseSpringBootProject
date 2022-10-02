package com.cozycats.cozycatsbackend.admin.Products;

import static org.assertj.core.api.Assertions.assertThat;
import com.cozycats.cozycatsbackend.admin.Product.ProductRepository;
import com.cozycats.cozycatscommon.entity.Brand;
import com.cozycats.cozycatscommon.entity.Category;
import com.cozycats.cozycatscommon.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateProduct(){
        Brand brand = entityManager.find(Brand.class, 10);
        Category category = entityManager.find(Category.class, 12);
        Product product = new Product();
        product.setName("Collar 2");
        product.setAlias("Collar for cats 2");
        product.setShortDescription("Short description for Collar 2");
        product.setFullDescription("Full description for Collar 2");

        product.setBrand(brand);
        product.setCategory(category);

        product.setPrice(678);
        product.setCost(600);
        product.setEnabled(true);
        product.setInStock(true);

        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        Product savedProduct = repo.save(product);
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
    }
    
    @Test
    public  void testListAllProducts(){
        Iterable<Product> all = repo.findAll();
        all.forEach(System.out::println);
    }

    @Test
    public void getProductTest(){
        Integer id = 2;
        Product byId = repo.findById(id).get();
        System.out.println(byId);
        assertThat(byId).isNotNull();
    }

    @Test
    public void updateProductTest(){
        Integer id = 1;
        Product byId = repo.findById(id).get();
        System.out.println("--------------------------------------------------");
        System.out.println(byId.getBrand().getName());
        byId.setPrice(1000);
        repo.save(byId);

        Product product = entityManager.find(Product.class, id);
        assertThat(product.getPrice()).isEqualTo(1000);
    }

    @Test
    public void deleteProductTEest(){
        Integer id = 2;
        repo.deleteById(id);
        Optional<Product> result = repo.findById(id);
        assertThat(!result.isPresent());
    }

    @Test
    public void testSaveProductWithImages(){
        Integer productId = 3;
        Product product = repo.findById(productId).get();
        product.setMainImage("image.jpg");
        product.addExtraImages("extra 1");
        product.addExtraImages("extra 2");
        product.addExtraImages("extra 3");
        Product savedProduct = repo.save(product);
        assertThat(savedProduct.getImages().size()).isEqualTo(3);
    }

    @Test
    public void testSaveProductWithDetails(){
        Integer productId = 11;
        Product product = repo.findById(productId).get();
        product.addDetail("Color", "White");
        product.addDetail("head circumference", "10-12\"");
        Product savedProduct = repo.save(product);
        assertThat(savedProduct.getDetails()).isNotEmpty();
    }
}
