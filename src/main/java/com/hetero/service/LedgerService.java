package com.hetero.service;

import com.hetero.models.Ledger;

public interface LedgerService {

    Ledger getLedger();
    Ledger updateLedger(Ledger newLedger);
    Ledger addLedger(Ledger ledger);
}
