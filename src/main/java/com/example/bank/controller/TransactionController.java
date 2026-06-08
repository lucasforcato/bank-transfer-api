package com.example.bank.controller;

import com.example.bank.dto.TransactionResponse;
import com.example.bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public List<TransactionResponse> findAll() {
        return transactionService.findAll();
    }

    @GetMapping("/account/{accountId}")
    public List<TransactionResponse> findByAccount(
            @PathVariable Long accountId) {

        return transactionService.findByAccountId(accountId);
    }
}