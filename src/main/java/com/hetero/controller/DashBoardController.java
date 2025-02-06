package com.hetero.controller;
import com.hetero.service.LedgerService;
import com.hetero.service.TransactionService;
import com.hetero.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashBoardController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    UserService userService;

    @Autowired
    LedgerService ledgerService;


    @GetMapping
    public String dashboard(Model model) {

        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("ledger", ledgerService.getLedger());
        return "user-management";

    }
}
