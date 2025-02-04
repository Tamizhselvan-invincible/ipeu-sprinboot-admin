package com.hetero.controller;

import com.hetero.models.Ledger;
import com.hetero.models.User;
import com.hetero.service.LedgerService;
import com.hetero.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LedgerService ledgerService;

    // Add User and update Ledger
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody @Valid User user) {
        User savedUser = userService.addUser(user);

        // Update Ledger
        Ledger ledger = ledgerService.getLedger();
        ledger.setNoOfUsers(ledger.getNoOfUsers() + 1);
        if (user.isBlocked()) {
            ledger.setNoOfBlockedUsers(ledger.getNoOfBlockedUsers() + 1);
        }
        ledgerService.updateLedger(ledger);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // Update User
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user) {
        User existingUser = userService.getUser(id);
        if (existingUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Check if the blocked status changed
        Ledger ledger = ledgerService.getLedger();
        if (existingUser.isBlocked() != user.isBlocked()) {
            if (user.isBlocked()) {
                ledger.setNoOfBlockedUsers(ledger.getNoOfBlockedUsers() + 1);
            } else {
                ledger.setNoOfBlockedUsers(ledger.getNoOfBlockedUsers() - 1);
            }
            ledgerService.updateLedger(ledger);
        }

        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    // Delete User and update Ledger
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        User user = userService.getUser(id);
        if (user == null) {
            return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
        }

        // Update Ledger
        Ledger ledger = ledgerService.getLedger();
        ledger.setNoOfUsers(ledger.getNoOfUsers() - 1);
        if (user.isBlocked()) {
            ledger.setNoOfBlockedUsers(ledger.getNoOfBlockedUsers() - 1);
        }
        ledgerService.updateLedger(ledger);

        String message = userService.deleteUser(id);
        return ResponseEntity.ok(message);
    }

    // Get User by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Integer id) {
        User user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }

    // Get All Users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Get All Unblocked Users
    @GetMapping("/unblocked")
    public ResponseEntity<List<User>> getAllUnBlockedUsers() {
        List<User> users = userService.getAllUnBlockedUsers();
        return ResponseEntity.ok(users);
    }

    // Get All Blocked Users
    @GetMapping("/blocked")
    public ResponseEntity<List<User>> getAllBlockedUsers() {
        List<User> users = userService.getAllBlockedUsers();
        return ResponseEntity.ok(users);
    }
}
