package com.hetero.repository;

import com.hetero.models.Ledger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LedgerDao extends JpaRepository<Ledger, Integer> {
    com.hetero.models.Ledger findFirstByOrderByIdAsc();
}
