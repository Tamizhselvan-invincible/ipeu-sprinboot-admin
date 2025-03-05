package com.hetero.service;

import com.hetero.models.Platform;
import com.hetero.models.Transaction;
import com.hetero.models.User;

import java.util.List;

public interface UserService {

    User addUser(User user);
    User updateUser(Long id,User user);
    String deleteUser(Long id);
    User getUser(Long id);

    void updateUserTransactions(User user);
    void updateUserCashBackTransactions(Long userId,Double cashBackAmount);

    List<Transaction> getUserTransactions(Long userId);
    List<User> getAllUsers();
    List<User> getAllUnBlockedUsers();
    List<User> getAllBlockedUsers();
    User updateUserBlockStatus(Long id, User newUser);

    void blockUser(Long userId);
    void unblockUser(Long userId);

    List<User> getUsersByPlatform(Platform platform);
    List<User> getUnblockedUsersByPlatform(Platform platform);
    List<User> getBlockedUsersByPlatform(Platform platform);

}
