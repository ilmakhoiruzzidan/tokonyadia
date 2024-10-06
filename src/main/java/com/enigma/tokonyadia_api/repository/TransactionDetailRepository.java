package com.enigma.tokonyadia_api.repository;

import com.enigma.tokonyadia_api.entity.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, String>, JpaSpecificationExecutor<TransactionDetail> {
}
