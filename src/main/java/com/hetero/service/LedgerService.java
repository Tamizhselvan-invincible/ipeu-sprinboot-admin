package com.hetero.service;

import com.hetero.models.Ledger;
import com.hetero.models.Transaction;

public interface LedgerService {

    Ledger getLedger();
    Ledger updateLedger(Ledger newLedger);
    Ledger addLedger(Ledger ledger);
    public Ledger updateLedgerWithRetry(LedgerServiceImpl.TransactionUpdate update);

}


//void updateLedger(Transaction transaction, boolean isDelete, Transaction oldTransaction);
//public void updateLedger(int users, int blockedUsers);