package com.hetero.service;

import com.hetero.models.Ledger;
import com.hetero.models.Transaction;
import com.hetero.models.TransactionStatus;
import com.hetero.repository.LedgerDao;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Optional;


@Slf4j
@Service
public class LedgerServiceImpl implements LedgerService {

    private static final int RETRY_ATTEMPTS = 3;
    private static final long RETRY_DELAY_MS = 100;

    @Autowired
    LedgerDao ledgerDao;




    @Transactional
    public Ledger getLedger() {
//      return ledgerDao.findById(1).orElseGet(this::createInitialLedger);
        return ledgerDao.findFirstByOrderByIdAsc().orElseThrow(() -> new EntityNotFoundException("Ledger not found"));
    }



    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    public Ledger updateLedgerWithRetry(TransactionUpdate update) {
        for (int attempt = 0; attempt < RETRY_ATTEMPTS; attempt++) {
            try {
                return doUpdateLedger(update);
            } catch (ObjectOptimisticLockingFailureException e) {
                if (attempt == RETRY_ATTEMPTS - 1) {
                    throw new ConcurrentModificationException("Failed to update ledger after " + RETRY_ATTEMPTS + " attempts");
                }
                try {
                    Thread.sleep(RETRY_DELAY_MS * (attempt + 1));
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Thread interrupted during retry", ie);
                }
            }
        }
        throw new RuntimeException("Failed to update ledger");
    }


    @Transactional(propagation = Propagation.MANDATORY)
     Ledger doUpdateLedger(TransactionUpdate update) {
        Ledger ledger = ledgerDao.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new EntityNotFoundException("Ledger not found"));

        if (update.getType() == UpdateType.TRANSACTION) {
            updateTransactionStats(ledger, update.getTransaction(), update.isDelete(), update.getOldTransaction());
        } else {
            updateUserStats(ledger, update.getUserUpdate());
        }

        return ledgerDao.saveAndFlush(ledger);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void updateTransactionStats(Ledger ledger, Transaction transaction,
                                        boolean isDelete, Transaction oldTransaction) {
        if (isDelete) {
            handleTransactionDeletion(ledger, oldTransaction);
        } else if (oldTransaction != null) {
            handleTransactionUpdate(ledger, transaction, oldTransaction);
        } else {
            handleNewTransaction(ledger, transaction);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void handleTransactionDeletion(Ledger ledger, Transaction oldTransaction) {
        ledger.setTotalAmount(ledger.getTotalAmount().subtract(oldTransaction.getAmount()));
        ledger.setTotalTransactions(ledger.getTotalTransactions() - 1);

        if (oldTransaction.getStatus() == TransactionStatus.Failed) {
            ledger.setTotalFailedAmount(ledger.getTotalFailedAmount()
                    .subtract(oldTransaction.getAmount()));
            ledger.setFailedTransactions(ledger.getFailedTransactions() - 1);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void handleTransactionUpdate(Ledger ledger, Transaction newTransaction,
                                         Transaction oldTransaction) {
        // Update total amount
        ledger.setTotalAmount(ledger.getTotalAmount()
                .subtract(oldTransaction.getAmount())
                .add(newTransaction.getAmount()));

        // Handle failed transaction status changes
        if (oldTransaction.getStatus() == TransactionStatus.Failed) {
            ledger.setTotalFailedAmount(ledger.getTotalFailedAmount()
                    .subtract(oldTransaction.getAmount()));
            ledger.setFailedTransactions(ledger.getFailedTransactions() - 1);
        }

        if (newTransaction.getStatus() == TransactionStatus.Failed) {
            ledger.setTotalFailedAmount(ledger.getTotalFailedAmount()
                    .add(newTransaction.getAmount()));
            ledger.setFailedTransactions(ledger.getFailedTransactions() + 1);
        }
    }


@Transactional
protected void handleNewTransaction(Ledger ledger, Transaction transaction) {
        ledger.setTotalAmount(ledger.getTotalAmount().add(transaction.getAmount()));
        ledger.setTotalTransactions(ledger.getTotalTransactions() + 1);

        if (transaction.getStatus() == TransactionStatus.Failed) {
            ledger.setTotalFailedAmount(ledger.getTotalFailedAmount()
                    .add(transaction.getAmount()));
            ledger.setFailedTransactions(ledger.getFailedTransactions() + 1);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void updateUserStats(Ledger ledger, UserUpdate userUpdate) {
        ledger.setNoOfUsers(userUpdate.getTotalUsers());
        ledger.setNoOfBlockedUsers(userUpdate.getBlockedUsers());
    }

    @Value
    public static class TransactionUpdate {
        UpdateType type;
        Transaction transaction;
        boolean delete;
        Transaction oldTransaction;
        UserUpdate userUpdate;
    }

    @Value
    public static class UserUpdate {
        int totalUsers;
        int blockedUsers;
    }

    public enum UpdateType {
        TRANSACTION,
        USER
    }


    @Transactional
    @Override
    public Ledger addLedger (Ledger ledger) {
        return ledgerDao.save(ledger);
    }

    @Transactional
    @Override
    public Ledger updateLedger (Ledger newLedger) {
        return ledgerDao.save(newLedger);

     }


    private Ledger createInitialLedger() {
        Ledger ledger = new Ledger();
        ledger.setId(1L);
        ledger.setFailedTransactions(0);
        ledger.setTotalTransactions(0);
        ledger.setTotalAmount(BigDecimal.ZERO);
        ledger.setTotalFailedAmount(BigDecimal.ZERO);
        ledger.setNoOfUsers(0);
        ledger.setNoOfBlockedUsers(0);
        ledger.setStatsDate(new Date());
        ledger.setVersion(1);
       return ledgerDao.save(ledger);
    }

}




//@Transactional
//@Override
//public void updateLedger(int users, int blockedUsers) {
//    Ledger ledger = ledgerDao.findById(1).orElseThrow(() -> new RuntimeException("Ledger not found"));
//
//    ledger.setNoOfUsers(users);
//    ledger.setNoOfBlockedUsers(blockedUsers);
//
//    ledgerDao.save(ledger);
//}
//@Transactional
//public synchronized void updateLedger(Transaction transaction, boolean isDelete, Transaction oldTransaction) {
//    Ledger ledger = ledgerDao.findById(1).orElseThrow(() -> new RuntimeException("Ledger not found"));
//
//    if (isDelete) {
//        // Handle deletion
//        ledger.setTotalAmount(ledger.getTotalAmount().subtract(oldTransaction.getAmount()));
//        ledger.setTotalTransactions(ledger.getTotalTransactions() - 1);
//
//        if (oldTransaction.getStatus() == TransactionStatus.Failed) {
//            ledger.setTotalFailedAmount(ledger.getTotalFailedAmount().subtract(oldTransaction.getAmount()));
//            ledger.setFailedTransactions(ledger.getFailedTransactions() - 1);
//        }
//    } else {
//        // Handle update or new transaction
//        if (oldTransaction != null) {  // If updating an existing transaction
//            ledger.setTotalAmount(ledger.getTotalAmount().subtract(oldTransaction.getAmount()).add(transaction.getAmount()));
//
//            if (oldTransaction.getStatus() == TransactionStatus.Failed) {
//                ledger.setTotalFailedAmount(ledger.getTotalFailedAmount().subtract(oldTransaction.getAmount()));
//                ledger.setFailedTransactions(ledger.getFailedTransactions() - 1);
//            }
//        } else { // If adding a new transaction
//            ledger.setTotalAmount(ledger.getTotalAmount().add(transaction.getAmount()));
//            ledger.setTotalTransactions(ledger.getTotalTransactions() + 1);
//        }
//
//        if (transaction.getStatus() == TransactionStatus.Failed) {
//            ledger.setTotalFailedAmount(ledger.getTotalFailedAmount().add(transaction.getAmount()));
//            ledger.setFailedTransactions(ledger.getFailedTransactions() + 1);
//        }
//    }
//
//    // Save ledger with versioning
//    ledgerDao.save(ledger);
//}
