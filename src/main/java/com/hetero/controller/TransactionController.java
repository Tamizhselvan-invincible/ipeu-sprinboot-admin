package com.hetero.controller;


import com.hetero.models.Ledger;
import com.hetero.models.Transaction;
import com.hetero.models.TransactionStatus;
import com.hetero.service.LedgerService;
import com.hetero.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private LedgerService ledgerService;

    // Add a new transaction and update Ledger
    @PostMapping("/add")
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction) {
        Transaction savedTransaction = transactionService.addTransaction(transaction);

        // Update ledger
        Ledger ledger = ledgerService.getLedger();
        ledger.setTotalAmount(ledger.getTotalAmount().add(transaction.getAmount()));
        ledger.setTotalTransactions(ledger.getTotalTransactions() + 1);
        if (TransactionStatus.Failed == transaction.getStatus()) {
            ledger.setTotalFailedAmount(ledger.getTotalFailedAmount().add(transaction.getAmount()));
            ledger.setFailedTransactions(ledger.getFailedTransactions() + 1);
        }
        ledgerService.updateLedger(ledger);

        return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
    }

    // Get all transactions
    @GetMapping("/all")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    // Get transaction by ID
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable int id) {
        Transaction transaction = transactionService.getTransactionById(id);
        return transaction != null ?
                new ResponseEntity<>(transaction, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete transaction by ID and update Ledger
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTransactionById(@PathVariable int id) {
        Transaction transaction = transactionService.getTransactionById(id);
        if (transaction == null) {
            return new ResponseEntity<>("Transaction Not Found", HttpStatus.NOT_FOUND);
        }

        // Update ledger before deleting
        Ledger ledger = ledgerService.getLedger();
        ledger.setTotalAmount(ledger.getTotalAmount().subtract(transaction.getAmount()));
        ledger.setTotalTransactions(ledger.getTotalTransactions() - 1);
        if (TransactionStatus.Failed == transaction.getStatus()) {
            ledger.setTotalFailedAmount(ledger.getTotalFailedAmount().subtract(transaction.getAmount()));
            ledger.setFailedTransactions(ledger.getFailedTransactions() - 1);
        }
        ledgerService.updateLedger(ledger);

        String message = transactionService.deleteTransactionById(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // Update a transaction and update Ledger
    @PutMapping("/update/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable int id, @RequestBody Transaction transaction) {
        Transaction existingTransaction = transactionService.getTransactionById(id);
        if (existingTransaction == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Update ledger before updating transaction
        Ledger ledger = ledgerService.getLedger();
        ledger.setTotalAmount(ledger.getTotalAmount().subtract(existingTransaction.getAmount()).add(transaction.getAmount()));
        if (TransactionStatus.Failed == existingTransaction.getStatus()) {
            ledger.setTotalFailedAmount(ledger.getTotalFailedAmount().subtract(existingTransaction.getAmount()));
            ledger.setFailedTransactions(ledger.getFailedTransactions() - 1);
        }
        if (TransactionStatus.Failed == transaction.getStatus()) {
            ledger.setTotalFailedAmount(ledger.getTotalFailedAmount().add(transaction.getAmount()));
            ledger.setFailedTransactions(ledger.getFailedTransactions() + 1);
        }
        ledgerService.updateLedger(ledger);

        Transaction updatedTransaction = transactionService.updateTransaction(id, transaction);
        return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
    }
}
