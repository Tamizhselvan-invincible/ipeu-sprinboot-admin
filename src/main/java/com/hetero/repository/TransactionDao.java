package com.hetero.repository;

import com.hetero.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionDao extends JpaRepository<Transaction, Integer> {
}
