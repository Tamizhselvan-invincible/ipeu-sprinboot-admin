package com.hetero.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsDao extends JpaRepository<com.hetero.models.Settings, Integer> {
    com.hetero.models.Settings findFirstByOrderByIdAsc();
}
