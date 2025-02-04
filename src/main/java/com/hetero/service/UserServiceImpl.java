package com.hetero.service;

import com.hetero.models.Transaction;
import com.hetero.models.User;
import com.hetero.repository.UserDao;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Transactional
    @Override
    public User addUser (User user) {

        if(user.getMPin()==null){
            SecureRandom secureRandom = new SecureRandom();
            int randomMPin = 1000 + secureRandom.nextInt(9000);
            user.setMPin(String.valueOf(randomMPin));

        }
        return userDao.save(user);
    }


    @Transactional
    @Override
    public User updateUser(Integer id, User newUser) {
        // Fetch the existing user from DB
        Optional<User> optionalUser = userDao.findById(id);

        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("User with ID " + id + " not found");
        }

        User existingUser = optionalUser.get();

        // Update only non-null fields
        if (newUser.getFirstName() != null) existingUser.setFirstName(newUser.getFirstName());
        if (newUser.getLastName() != null) existingUser.setLastName(newUser.getLastName());
        if (newUser.getEmail() != null) existingUser.setEmail(newUser.getEmail());
        if (newUser.getMobileNo() != null) existingUser.setMobileNo(newUser.getMobileNo());
        if (newUser.getMPin() != null) existingUser.setMPin(newUser.getMPin());
        if (newUser.getProfilePicture() != null) existingUser.setProfilePicture(newUser.getProfilePicture());
        if (newUser.getAccountStatus() != null) existingUser.setAccountStatus(newUser.getAccountStatus());
        if (newUser.getAppVersion() != null) existingUser.setAppVersion(newUser.getAppVersion());
        if (newUser.getLastLoginTime() != null) existingUser.setLastLoginTime(newUser.getLastLoginTime());
        if (newUser.getUserRole() != null) existingUser.setUserRole(newUser.getUserRole());
        if (newUser.getAppUpdatedAt() != null) existingUser.setAppUpdatedAt(newUser.getAppUpdatedAt());
        if (newUser.getDeviceBrandName() != null) existingUser.setDeviceBrandName(newUser.getDeviceBrandName());
        if (newUser.getDeviceVersionCode() != null) existingUser.setDeviceVersionCode(newUser.getDeviceVersionCode());
        if (newUser.getOsType() != null) existingUser.setOsType(newUser.getOsType());

        // Handle boolean field updates
        existingUser.setBlocked(newUser.isBlocked());

        // Handle transactions (if provided)
        if (newUser.getTransactions() != null && !newUser.getTransactions().isEmpty()) {
            for (Transaction transaction : newUser.getTransactions()) {
                transaction.setUser(existingUser); // Ensure proper association
            }
            existingUser.getTransactions().clear();
            existingUser.getTransactions().addAll(newUser.getTransactions());
        }

        // Save the updated user
        return userDao.save(existingUser);
    }


    @Transactional
    @Override
    public String deleteUser (Integer id) {
        Optional<User> optionalUser = userDao.findById(id);
        if (optionalUser.isEmpty()) {
            throw new EntityNotFoundException("User with ID " + id + " not found");
        }else {
            User user = optionalUser.get();
            userDao.delete(user);
            return "User with ID " + id + " deleted";
        }
    }

    @Transactional
    @Override
    public User getUser (Integer id) {
        return userDao.findById(id).orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " not found"));
    }

    @Transactional
    @Override
    public List<User> getAllUsers () {
        return userDao.findAll();
    }

    @Transactional
    @Override
    public List<User> getAllUnBlockedUsers() {
        return userDao.findByIsBlockedFalse();
    }

    @Transactional
    @Override
    public List<User> getAllBlockedUsers() {
        return userDao.findByIsBlockedTrue();
    }

}
