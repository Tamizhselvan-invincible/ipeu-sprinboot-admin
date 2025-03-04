package com.hetero.repository;

import com.hetero.models.Platform;
import com.hetero.models.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserDao extends JpaRepository<User, Long> {

    // Fetch all unblocked users
    List<User> findByIsBlockedFalse();

    // Fetch all blocked users
    List<User> findByIsBlockedTrue();

    @EntityGraph(attributePaths = {"transactions"})
    List<User> findAll();

    Optional<User> findByFirstName(String username);

    Optional<User> findByMobileNo(String email);


    Optional<User> findByEmail(String email);


    @EntityGraph(attributePaths = "transactions")
    Optional<User> findById(Long id);

        List<User> findByPlatformType(Platform platformType);

        List<User> findByPlatformTypeAndIsBlockedFalse(Platform platformType);

        List<User> findByPlatformTypeAndIsBlockedTrue(Platform platformType);

}
