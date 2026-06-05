
package com.example.bank.service;

import com.example.bank.dto.CreateAccountRequest;
import com.example.bank.entity.Account;
import com.example.bank.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository repo;

    public AccountService(AccountRepository r) {
        repo = r;
    }

    public Account create(CreateAccountRequest req) {
        Account a = new Account();
        a.setName(req.name());
        a.setBalance(req.initialBalance());
        return repo.save(a);
    }

    public List<Account> findAll() {
        return repo.findAll();
    }
}
