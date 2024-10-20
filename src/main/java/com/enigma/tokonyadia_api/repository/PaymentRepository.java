package com.enigma.tokonyadia_api.repository;

import com.enigma.tokonyadia_api.entity.Payment;
import com.enigma.tokonyadia_api.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String>, JpaSpecificationExecutor<Payment> {
    Optional<Payment> findByOrder_Id(String orderId);
}
