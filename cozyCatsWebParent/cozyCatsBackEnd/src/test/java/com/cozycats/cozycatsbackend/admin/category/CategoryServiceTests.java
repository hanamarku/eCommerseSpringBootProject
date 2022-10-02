package com.cozycats.cozycatsbackend.admin.category;

import com.cozycats.cozycatsbackend.admin.Category.CategoryRepository;
import com.cozycats.cozycatsbackend.admin.Category.CategoryService;
import com.cozycats.cozycatscommon.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)

public class CategoryServiceTests {
    @MockBean
    private CategoryRepository repo;

    @InjectMocks
    private CategoryService service;

    @Test
    public void testCheckUniqueName(){
        Integer id = null;
        String name = "Toys";
        String alias = "abc";

        Category category = new Category(id, name, alias);

        Mockito.when(repo.findByName(name)).thenReturn(category);
        Mockito.when(repo.findByAlias(alias)).thenReturn(null);
        String s = service.checkUnique(id, name, alias);

        assertThat(s).isEqualTo("DuplicateName");
    }

    @Test
    public void testCheckUniqueInNewModeReturnDuplicateAlias() {
        Integer id = null;
        String name = "NameABC";
        String alias = "Toys";

        Category category = new Category(id, name, alias);

        Mockito.when(repo.findByName(name)).thenReturn(null);
        Mockito.when(repo.findByAlias(alias)).thenReturn(category);

        String result = service.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("DuplicateAlias");
    }

    @Test
    public void testCheckUniqueInNewModeReturnOK() {
        Integer id = null;
        String name = "NameABC";
        String alias = "Toys";

        Mockito.when(repo.findByName(name)).thenReturn(null);
        Mockito.when(repo.findByAlias(alias)).thenReturn(null);

        String result = service.checkUnique(id, name, alias);

        assertThat(result).isEqualTo("OK");
    }


}
