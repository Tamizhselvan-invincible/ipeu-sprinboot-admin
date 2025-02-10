package com.hetero.repository;

import com.hetero.models.Platform;
import com.hetero.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TransactionDao extends JpaRepository<Transaction, Integer> {

    @Query("SELECT t FROM Transaction t WHERE t.userId = :userId")
    List<Transaction> findByUserId(@Param("userId") Integer userId);

    List<Transaction> findByPlatformType(Platform platformType);
}
