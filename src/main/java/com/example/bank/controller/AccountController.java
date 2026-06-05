
package com.example.bank.controller;

import com.example.bank.dto.*;
import com.example.bank.entity.Account;
import com.example.bank.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService service;

    public AccountController(AccountService s) {
        service = s;
    }

    @PostMapping
    public Account create(@RequestBody CreateAccountRequest r) {
        return service.create(r);
    }

    @GetMapping
    public List<Account> all() {
        return service.findAll();
    }
}
