
package com.example.bank.service;

import com.example.bank.dto.CreateAccountRequest;
import com.example.bank.entity.Account;
import com.example.bank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repo;

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
