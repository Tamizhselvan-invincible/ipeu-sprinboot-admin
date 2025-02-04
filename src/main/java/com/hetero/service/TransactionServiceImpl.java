package com.hetero.service;

import com.hetero.models.Transaction;
import com.hetero.repository.TransactionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionDao transactionDao;

    @Transactional
    @Override
    public Transaction addTransaction (Transaction transaction) {
        return transactionDao.save(transaction);
    }

    @Transactional
    @Override
    public List<Transaction> getAllTransactions () {
        return transactionDao.findAll();
    }

    @Transactional
    @Override
    public Transaction getTransactionById (int id) {
        return transactionDao.findById(id).get();
    }

    @Transactional
    @Override
    public String deleteTransactionById (int id) {

        if (transactionDao.existsById(id)) {
            transactionDao.deleteById(id);
            return "Transaction deleted";
        }else
            return "Transaction Not Found";

    }

    @Transactional
    @Override
    public Transaction updateTransaction (int id, Transaction transaction) {

        Transaction existingTransaction = transactionDao.findById(id).get();
        if (existingTransaction != null) {
           existingTransaction.setAmount(transaction.getAmount());
           existingTransaction.setAggregatedTransactionId(transaction.getAggregatedTransactionId());
           existingTransaction.setTransactionReference(transaction.getTransactionReference());
           existingTransaction.setDeleted(transaction.isDeleted());
           existingTransaction.setUser(transaction.getUser());
           existingTransaction.setCashBack(transaction.getCashBack());
           existingTransaction.setStatus(transaction.getStatus());
           existingTransaction.setDeletedAt(transaction.getDeletedAt());
           transactionDao.save(existingTransaction);
            return transaction;
        }else {
            return null;
        }

    }
}
