package com.hetero.service;

import com.hetero.models.Transaction;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentVerificationService {

    @Autowired
    TransactionService transactionService;

    @Autowired
    UserService userService;

    public boolean verifyTransaction(int transactionId) {
        return true;
    }


}
