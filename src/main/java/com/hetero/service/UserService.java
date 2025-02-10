package com.hetero.service;

import com.hetero.models.Platform;
import com.hetero.models.Transaction;
import com.hetero.models.User;

import java.util.List;

public interface UserService {

    User addUser(User user);
    User updateUser(Integer id,User user);
    String deleteUser(Integer id);
    User getUser(Integer id);

    void updateUserTransactions(User user);

    List<Transaction> getUserTransactions(Integer userId);
    List<User> getAllUsers();
    List<User> getAllUnBlockedUsers();
    List<User> getAllBlockedUsers();
    User updateUserBlockStatus(Integer id, User newUser);

    void blockUser(Integer userId);
    void unblockUser(Integer userId);

    List<User> getUsersByPlatform(Platform platform);
    List<User> getUnblockedUsersByPlatform(Platform platform);
    List<User> getBlockedUsersByPlatform(Platform platform);

}
