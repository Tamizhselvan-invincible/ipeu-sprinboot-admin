package com.hetero.controller;

import com.hetero.models.Ledger;
import com.hetero.models.User;
import com.hetero.service.LedgerService;
import com.hetero.service.LedgerServiceImpl;
import com.hetero.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ConcurrentModificationException;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private LedgerService ledgerService;

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody @Valid User user) {
        User savedUser = userService.addUser(user);
        try {
            ledgerService.updateLedgerWithRetry(new LedgerServiceImpl.TransactionUpdate(
                    LedgerServiceImpl.UpdateType.USER,
                    null,
                    false,
                    null,
                    new LedgerServiceImpl.UserUpdate(
                            ledgerService.getLedger().getNoOfUsers() + 1,
                            user.isBlocked() ? ledgerService.getLedger().getNoOfBlockedUsers() + 1
                                    : ledgerService.getLedger().getNoOfBlockedUsers()
                    )
            ));
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (ConcurrentModificationException e) {
            log.error("Failed to update ledger for new user: {}", savedUser.getId(), e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        User user = userService.getUser(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            ledgerService.updateLedgerWithRetry(new LedgerServiceImpl.TransactionUpdate(
                    LedgerServiceImpl.UpdateType.USER,
                    null,
                    false,
                    null,
                    new LedgerServiceImpl.UserUpdate(
                            ledgerService.getLedger().getNoOfUsers() - 1,
                            user.isBlocked() ? ledgerService.getLedger().getNoOfBlockedUsers() - 1
                                    : ledgerService.getLedger().getNoOfBlockedUsers()
                    )
            ));
            String message = userService.deleteUser(id);
            return ResponseEntity.ok(message);
        } catch (ConcurrentModificationException e) {
            log.error("Failed to update ledger for user deletion: {}", id, e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Failed to update ledger");
        }
    }

    @PostMapping("/block")
    public ResponseEntity<?> blockUser(@RequestParam Integer userId) {
        User user = userService.getUser(userId);
        if (user == null || user.isBlocked()) {
            return ResponseEntity.badRequest().body("User Not Found or Already Blocked");
        }

        try {
            userService.blockUser(userId);
            ledgerService.updateLedgerWithRetry(new LedgerServiceImpl.TransactionUpdate(
                    LedgerServiceImpl.UpdateType.USER,
                    null,
                    false,
                    null,
                    new LedgerServiceImpl.UserUpdate(
                            ledgerService.getLedger().getNoOfUsers(),
                            ledgerService.getLedger().getNoOfBlockedUsers() + 1
                    )
            ));
            return ResponseEntity.ok().build();
        } catch (ConcurrentModificationException e) {
            log.error("Failed to update ledger for blocking user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/unblock")
    public ResponseEntity<?> unblockUser(@RequestParam Integer userId) {
        User user = userService.getUser(userId);
        if (user == null || !user.isBlocked()) {
            return ResponseEntity.badRequest().body("User Not Found or Already Unblocked");
        }

        try {
            userService.unblockUser(userId);
            ledgerService.updateLedgerWithRetry(new LedgerServiceImpl.TransactionUpdate(
                    LedgerServiceImpl.UpdateType.USER,
                    null,
                    false,
                    null,
                    new LedgerServiceImpl.UserUpdate(
                            ledgerService.getLedger().getNoOfUsers(),
                            ledgerService.getLedger().getNoOfBlockedUsers() - 1
                    )
            ));
            return ResponseEntity.ok().build();
        } catch (ConcurrentModificationException e) {
            log.error("Failed to update ledger for unblocking user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    // Read operations - unchanged
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user) {
        User existingUser = userService.getUser(id);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Integer id) {
        User user = userService.getUser(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/unblocked")
    public ResponseEntity<List<User>> getAllUnBlockedUsers() {
        return ResponseEntity.ok(userService.getAllUnBlockedUsers());
    }

    @GetMapping("/blocked")
    public ResponseEntity<List<User>> getAllBlockedUsers() {
        return ResponseEntity.ok(userService.getAllBlockedUsers());
    }
}

