package com.cozycats.Category;

import com.cozycats.cozycatscommon.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repo;

    @Test
    public void testListEnabledCategories(){
        List<Category> categories = repo.findAllEnabled();
        categories.forEach(category -> {
            System.out.println(category.getName() + " is " + category.isEnabled() + "\n");
        });
    }

    @Test
    public void testFindCategoriesByAlias(){
        String alias = "Cat Toys";
        Category byAliasEnabled = repo.findByAliasEnabled(alias);
        assertThat(byAliasEnabled).isNotNull();
    }
}
