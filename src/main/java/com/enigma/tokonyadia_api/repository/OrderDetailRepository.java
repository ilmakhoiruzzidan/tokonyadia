package com.enigma.tokonyadia_api.repository;

import com.enigma.tokonyadia_api.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, String>, JpaSpecificationExecutor<OrderDetail> {
}
