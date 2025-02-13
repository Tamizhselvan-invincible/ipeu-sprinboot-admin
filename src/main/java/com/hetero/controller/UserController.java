package com.hetero.controller;

import com.hetero.exception.UserNotFoundException;
import com.hetero.models.Platform;
import com.hetero.models.Transaction;
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
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
@CrossOrigin(origins = "https://moving-raccoon-fleet.ngrok-free.app")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private LedgerService ledgerService;

    ///POST MAPPINGS
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


    /// Delete Mapping

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        User user = userService.getUser(id);
        if (user == null) {
            throw new UserNotFoundException("User with ID " + id + " not found");
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
            return ResponseEntity.ok(Map.of("message", message));

        } catch (ConcurrentModificationException e) {
            log.error("Failed to update ledger for user deletion: {}", id, e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Failed to update ledger");
        }
    }


    ///Put Mapping


    // Read operations - unchanged
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user) {
        User existingUser = userService.getUser(id);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userService.updateUser(id, user));
    }


    ///GET MAPPINGS

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<Transaction>> getUserTransactions(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.getUserTransactions(id));
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


    ///GET Mapping For Platform Specific Fetching

       @GetMapping("/platform/{platform}")
        public ResponseEntity<List<User>> getUsersByPlatform(@PathVariable Platform platform) {
            return ResponseEntity.ok(userService.getUsersByPlatform(platform));
        }

        @GetMapping("/platform/{platform}/unblocked")
        public ResponseEntity<List<User>> getUnblockedUsersByPlatform(@PathVariable Platform platform) {
            return ResponseEntity.ok(userService.getUnblockedUsersByPlatform(platform));
        }

        @GetMapping("/platform/{platform}/blocked")
        public ResponseEntity<List<User>> getBlockedUsersByPlatform(@PathVariable Platform platform) {
            return ResponseEntity.ok(userService.getBlockedUsersByPlatform(platform));
        }


}

