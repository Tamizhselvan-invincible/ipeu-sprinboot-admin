package com.hetero.repository;

import com.hetero.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserDao extends JpaRepository<User, Integer> {

    // Fetch all unblocked users
    List<User> findByIsBlockedFalse();

    // Fetch all blocked users
    List<User> findByIsBlockedTrue();
}
