package com.cozycats.Category;

import Exceptions.CategoryNotFoundException;
import Exceptions.ProductNotFoundException;
import com.cozycats.cozycatscommon.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repo;

    public List<Category> listNoChildrenCategories(){
        List<Category> listNoChildren = new ArrayList<>();
        List<Category> allEnabled = repo.findAllEnabled();
        allEnabled.forEach(category -> {
            Set<Category> children = category.getChildren();
            if(children == null || children.size() == 0){
                listNoChildren.add(category);
            }
        });
        return listNoChildren;
    }

    public Category getCategory(String alias) throws CategoryNotFoundException {
        Category byAliasEnabled = repo.findByAliasEnabled(alias);

        if(byAliasEnabled == null){
            throw new CategoryNotFoundException("Could not find any category with alias " + alias);
        }
        return byAliasEnabled;
    }

    public List<Category> getCategoryParents(Category child){
        List<Category> listParents = new ArrayList<>();
        Category parent = child.getParent();
        while (parent != null){
            listParents.add(0, parent);
            parent = parent.getParent();
        }
        listParents.add(child);
        return listParents;
    }
}
