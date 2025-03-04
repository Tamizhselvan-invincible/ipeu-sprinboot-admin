package com.hetero.service;

import com.hetero.models.Platform;
import com.hetero.models.Transaction;
import com.hetero.utils.ApiResponse;

import java.util.List;

public interface TransactionService {

    Transaction addTransaction(Transaction transaction);
    List<Transaction> getAllTransactions();
    Transaction getTransactionById(Long id);
    List<Transaction> getAllTransactionsByPlatformType(Platform platform);
    ApiResponse<String> deleteTransactionById(Long id);
    ApiResponse<Transaction> updateTransaction(Long id, Transaction transaction);
}
