package com.hetero.service;

import com.hetero.models.Transaction;
import com.hetero.repository.TransactionDao;

import java.util.List;

public interface TransactionService {

    Transaction addTransaction(Transaction transaction);
    List<Transaction> getAllTransactions();
    Transaction getTransactionById(int id);
    String deleteTransactionById(int id);
    Transaction updateTransaction(int id, Transaction transaction);
}
