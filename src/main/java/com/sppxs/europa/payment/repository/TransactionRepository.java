package com.sppxs.europa.payment.repository;

import com.sppxs.europa.payment.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByTransactionId(String transactionId);
}
