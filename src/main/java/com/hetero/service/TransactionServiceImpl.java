package com.hetero.service;

import com.hetero.models.Platform;
import com.hetero.models.SubscriptionPlan;
import com.hetero.models.Transaction;
import com.hetero.models.User;
import com.hetero.repository.SubscriptionPlanDao;
import com.hetero.repository.TransactionDao;
import com.hetero.utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);
    @Autowired
    TransactionDao transactionDao;

    @Autowired
    SubscriptionPlanDao subscriptionPlanDao;

    @Autowired
    UserService userService;

    @Autowired
    private LedgerService ledgerService;

    @Transactional
    @Override
    public Transaction addTransaction (Transaction transaction) {

        User user = userService.getUser(transaction.getUserId());
        List<Transaction> transactions = user.getTransactions();
        if (transactions.isEmpty()) {
            transactions = new ArrayList<>();
        }
        transactions.add(transaction);
        user.setTransactions(transactions);
        if (transaction.getSubscriptionPlan() != null) {
            SubscriptionPlan plan = subscriptionPlanDao.save(transaction.getSubscriptionPlan());
            transaction.setSubscriptionPlan(plan);
        }

        Transaction savedTransaction =  transactionDao.save(transaction);
        try {
            ledgerService.updateLedgerWithRetry(new LedgerServiceImpl.TransactionUpdate(
                    LedgerServiceImpl.UpdateType.TRANSACTION,
                    savedTransaction,
                    false,
                    null,
                    null
            ));
        } catch (ConcurrentModificationException e) {
            log.error("Failed to update ledger for transaction: {}", savedTransaction.getId(), e);
        }

        return savedTransaction;
    }

    @Transactional
    @Override
    public List<Transaction> getAllTransactions () {
        return transactionDao.findAll();
    }


    @Transactional
    @Override
    public List<Transaction> getAllTransactionsByPlatformType(Platform platform){
        return transactionDao.findByPlatformType(platform);
    }


    @Transactional
    @Override
    public Transaction getTransactionById (Long id) {
        return transactionDao.findById(id).get();
    }

    @Transactional
    @Override
    public ApiResponse<String> deleteTransactionById(Long id) {
        Optional<Transaction> optionalTransaction = transactionDao.findById(id);

        if (optionalTransaction.isEmpty()) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Transaction Not Found", null);
        }

        Transaction transaction = optionalTransaction.get();

        try {
            ledgerService.updateLedgerWithRetry(new LedgerServiceImpl.TransactionUpdate(
                    LedgerServiceImpl.UpdateType.TRANSACTION,
                    transaction,
                    true,
                    transaction,
                    null
            ));

            transaction.setDeleted(true);
            transaction.setDeletedAt(new Date());
            transactionDao.save(transaction); // Soft delete

            return new ApiResponse<>(HttpStatus.OK.value(), "Transaction deleted successfully", null);
        } catch (ConcurrentModificationException e) {
            log.error("Failed to update ledger for deletion of transaction: {}", id, e);
            return new ApiResponse<>(HttpStatus.CONFLICT.value(), "Failed to update ledger", null);
        }
    }


    @Transactional
    @Override
    public ApiResponse<Transaction> updateTransaction(Long id, Transaction transaction) {
        Optional<Transaction> optionalTransaction = transactionDao.findById(id);

        if (optionalTransaction.isEmpty()) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Transaction not found", null);
        }

        Transaction existingTransaction = optionalTransaction.get();

        existingTransaction.setAmount(transaction.getAmount());
        existingTransaction.setAggregatedTransactionId(transaction.getAggregatedTransactionId());
        existingTransaction.setTransactionReference(transaction.getTransactionReference());
        existingTransaction.setDeleted(transaction.isDeleted());
        existingTransaction.setUserId(transaction.getUserId());
        existingTransaction.setCashBack(transaction.getCashBack());
        existingTransaction.setStatus(transaction.getStatus());
        existingTransaction.setDeletedAt(transaction.getDeletedAt());

        Transaction updatedTransaction = transactionDao.save(existingTransaction);

        return new ApiResponse<>(HttpStatus.OK.value(), "Transaction updated successfully", updatedTransaction);
    }

}
