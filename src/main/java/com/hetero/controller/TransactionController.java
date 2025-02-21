package com.hetero.controller;


import com.hetero.models.Platform;
import com.hetero.models.Transaction;
import com.hetero.service.LedgerService;
import com.hetero.service.LedgerServiceImpl;
import com.hetero.service.TransactionService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ConcurrentModificationException;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@CrossOrigin(origins = "https://moving-raccoon-fleet.ngrok-free.app")
public class TransactionController {
    private static final Logger log = LogManager.getLogger(TransactionController.class);
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private LedgerService ledgerService;

    @PostMapping("/add")
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction) {
        Transaction savedTransaction = transactionService.addTransaction(transaction);
        try {
            ledgerService.updateLedgerWithRetry(new LedgerServiceImpl.TransactionUpdate(
                    LedgerServiceImpl.UpdateType.TRANSACTION,
                    savedTransaction,
                    false,
                    null,
                    null
            ));
            return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
        } catch (ConcurrentModificationException e) {
            log.error("Failed to update ledger for transaction: {}", savedTransaction.getId(), e);

            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }


    @GetMapping("/all/{platform}")
    public ResponseEntity<?> getAllTransactionsByPlatform(@PathVariable String platform) {
        try {
            // Convert String to Enum (Case-sensitive validation)
            Platform platformEnum = Platform.valueOf(platform);

            // Fetch transactions by platform type
            return ResponseEntity.ok(transactionService.getAllTransactionsByPlatformType(platformEnum));
        } catch (IllegalArgumentException e) {
            // Return 400 Bad Request if the platform is invalid
            return ResponseEntity.badRequest().body("Invalid platform type. Allowed values: iPeyU, Secondary, ALL");
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable int id) {
        Transaction transaction = transactionService.getTransactionById(id);
        if (transaction == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTransactionById(@PathVariable int id) {
        Transaction transaction = transactionService.getTransactionById(id);

        if (transaction == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            ledgerService.updateLedgerWithRetry(new LedgerServiceImpl.TransactionUpdate(
                    LedgerServiceImpl.UpdateType.TRANSACTION,
                    transaction,
                    true,
                    transaction,
                    null
            ));
            String message = transactionService.deleteTransactionById(id);
            return ResponseEntity.ok(message);
        } catch (ConcurrentModificationException e) {
            log.error("Failed to update ledger for deletion of transaction: {}", id, e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Failed to update ledger");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable int id, @RequestBody Transaction transaction) {
        Transaction existingTransaction = transactionService.getTransactionById(id);
        if (existingTransaction == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            ledgerService.updateLedgerWithRetry(new LedgerServiceImpl.TransactionUpdate(
                    LedgerServiceImpl.UpdateType.TRANSACTION,
                    transaction,
                    false,
                    existingTransaction,
                    null
            ));
            Transaction updatedTransaction = transactionService.updateTransaction(id, transaction);
            return ResponseEntity.ok(updatedTransaction);
        } catch (ConcurrentModificationException e) {
            log.error("Failed to update ledger for transaction update: {}", id, e);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}