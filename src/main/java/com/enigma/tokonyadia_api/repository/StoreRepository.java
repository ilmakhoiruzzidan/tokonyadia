package com.enigma.tokonyadia_api.repository;

import com.enigma.tokonyadia_api.entity.Customer;
import com.enigma.tokonyadia_api.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, String>{
    Page<Store> findAllByNameIsLikeIgnoreCase(String name, Pageable pageable);
}
