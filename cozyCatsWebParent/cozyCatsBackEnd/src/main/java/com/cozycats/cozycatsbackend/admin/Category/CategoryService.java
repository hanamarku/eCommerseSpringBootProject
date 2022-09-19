package com.cozycats.cozycatsbackend.admin.Category;

import com.cozycats.cozycatscommon.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.persistence.SecondaryTable;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repo;

    public List<Category> listAll(){
        return (List<Category>) repo.findAll();
    }

}
