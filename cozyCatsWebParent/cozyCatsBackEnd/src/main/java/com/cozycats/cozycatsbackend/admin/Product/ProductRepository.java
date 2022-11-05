package com.cozycats.cozycatsbackend.admin.Product;

import com.cozycats.cozycatscommon.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

    public Long countById(Integer id);

    public Product findByName(String name);

    @Query("update  Product p set p.enabled = ?2 where p.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer id, boolean enabled);

    @Query("Select p from Product p where p.name like %?1% or p.shortDescription like %?1%" +
            "or p.fullDescription like %?1% or p.brand.name like %?1% or p.category.name like %?1%")
    public Page<Product> findAll(String keyword, Pageable pageable);

    @Query("select p from Product p where p.category.id = ?1 or p.category.allParentsIDS like %?2")
    public Page<Product> findAllInCategory(Integer categoryId, String categoryIdMatch, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (p.category.id = ?1 "
            + "OR p.category.allParentsIDS LIKE %?2%) AND "
            + "(p.name LIKE %?3% "
            + "OR p.shortDescription LIKE %?3% "
            + "OR p.fullDescription LIKE %?3% "
            + "OR p.brand.name LIKE %?3% "
            + "OR p.category.name LIKE %?3%)")
    public Page<Product> searchInCategory(Integer categoryId, String categoryIdMatch, String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
    public Page<Product> searchProductsByName(String keyword, Pageable pageable);

}
