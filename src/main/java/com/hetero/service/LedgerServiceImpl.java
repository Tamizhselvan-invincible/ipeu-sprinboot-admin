package com.hetero.service;

import com.hetero.models.Ledger;
import com.hetero.repository.LedgerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class LedgerServiceImpl implements LedgerService {

    @Autowired
    LedgerDao ledgerDao;

    @Override
    public Ledger getLedger () {
        Ledger ledger = ledgerDao.findFirstByOrderByIdAsc();
        if (ledger == null) {
            ledger = new Ledger();
            ledger.setId((long) 1);
            ledger.setFailedTransactions(0);
            ledger.setTotalAmount( new BigDecimal("0.00"));
            ledger.setTotalFailedAmount( new BigDecimal("0.00"));
            ledger.setNoOfUsers(0);
            ledger.setNoOfBlockedUsers(0);
            ledger.setFailedTransactions(0);
            ledger.setStatsDate(new Date());
            ledgerDao.save(ledger);
           return  ledger;
        }
        return ledgerDao.findFirstByOrderByIdAsc();
    }

    @Transactional
    @Override
    public Ledger updateLedger (Ledger newLedger) {
        Ledger ledger = ledgerDao.findFirstByOrderByIdAsc();
        if (ledger != null) {
            ledger.setFailedTransactions(newLedger.getFailedTransactions());
            ledger.setTotalAmount(newLedger.getTotalAmount());
            ledger.setTotalFailedAmount(newLedger.getTotalFailedAmount());
            ledger.setNoOfUsers(newLedger.getNoOfUsers());
            ledger.setNoOfBlockedUsers(newLedger.getNoOfBlockedUsers());
            ledger.setFailedTransactions(newLedger.getFailedTransactions());
            ledger.setStatsDate(newLedger.getStatsDate());
            ledgerDao.save(ledger);
            return  ledger;
        }
        return ledgerDao.save(newLedger);
    }

    @Transactional
    @Override
    public Ledger addLedger (Ledger ledger) {
        return ledgerDao.save(ledger);
    }
}
