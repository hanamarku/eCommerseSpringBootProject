package com.cozycats.cozycatsbackend.admin.Order;

import com.cozycats.cozycatsbackend.admin.paging.SearchRepository;
import com.cozycats.cozycatscommon.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

public interface OrderRepository extends PagingAndSortingRepository<Order, Integer> {

    @Query("SELECT o FROM Order o WHERE o.firstName LIKE %?1% OR"
            + " o.lastName LIKE %?1% OR o.phoneNumber LIKE %?1% OR"
            + " o.addressLine1 LIKE %?1% OR o.addressLine2 LIKE %?1% OR"
            + " o.postalCode LIKE %?1% OR o.city LIKE %?1% OR"
            + " o.state LIKE %?1% OR o.country LIKE %?1% OR"
            + " o.paymentMethod LIKE %?1% OR o.status LIKE %?1% OR"
            + " o.customer.firstName LIKE %?1% OR"
            + " o.customer.lastName LIKE %?1%")
    public Page<Order> findAll(String keyword, Pageable pageable);

    public Long countById(Integer id);
}

