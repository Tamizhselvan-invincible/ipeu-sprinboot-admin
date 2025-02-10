package com.hetero.service;

import com.hetero.models.Platform;
import com.hetero.models.SubscriptionPlan;
import com.hetero.models.Transaction;
import com.hetero.models.User;
import com.hetero.repository.SubscriptionPlanDao;
import com.hetero.repository.TransactionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionDao transactionDao;

    @Autowired
    SubscriptionPlanDao subscriptionPlanDao;

    @Autowired
    UserService userService;


    @Transactional
    @Override
    public Transaction addTransaction (Transaction transaction) {
        User user = userService.getUser(transaction.getUserId());
        List<Transaction> transactions = user.getTransactions();
        transactions.add(transaction);
        user.setTransactions(transactions);
        SubscriptionPlan plan = subscriptionPlanDao.save(transaction.getSubscriptionPlan());
        transaction.setSubscriptionPlan(plan);
        return transactionDao.save(transaction);
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
    public Transaction getTransactionById (int id) {
        return transactionDao.findById(id).get();
    }

    @Transactional
    @Override
    public String deleteTransactionById (int id) {

        if (transactionDao.existsById(id)) {
            Transaction transaction = transactionDao.findById(id).get();
            transaction.setDeleted(true);
            transaction.setDeletedAt(new Date());
            transactionDao.save(transaction);
//            transactionDao.deleteById(id);
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
           existingTransaction.setUserId(transaction.getUserId());
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
