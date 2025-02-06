package com.hetero.service;

import com.hetero.models.User;

import java.util.List;

public interface UserService {

    User addUser(User user);
    User updateUser(Integer id,User user);
    String deleteUser(Integer id);
    User getUser(Integer id);
    List<User> getAllUsers();
    List<User> getAllUnBlockedUsers();
    List<User> getAllBlockedUsers();
    User updateUserBlockStatus(Integer id, User newUser);
    public void blockUser(Integer userId);
    public void unblockUser(Integer userId);
}
