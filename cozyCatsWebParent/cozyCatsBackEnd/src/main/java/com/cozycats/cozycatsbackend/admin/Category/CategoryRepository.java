package com.cozycats.cozycatsbackend.admin.Category;

import com.cozycats.cozycatscommon.entity.Category;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.Column;

@Repository

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {

}
