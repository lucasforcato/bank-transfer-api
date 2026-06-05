
package com.example.bank.service;

import com.example.bank.dto.TransferRequest;
import com.example.bank.entity.*;
import com.example.bank.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.exception.InsufficientBalanceException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationService notificationService;


    @Transactional
    public void transfer(TransferRequest r) {
        Account src = accountRepository.findById(r.sourceAccountId())
                .orElseThrow(() ->
                        new AccountNotFoundException(r.sourceAccountId()));
        Account dst = accountRepository.findById(r.destinationAccountId())
                .orElseThrow(() ->
                        new AccountNotFoundException(r.destinationAccountId()));
        if (src.getBalance().compareTo(r.amount()) < 0) {
            throw new InsufficientBalanceException(src.getId());
        }
        src.setBalance(src.getBalance().subtract(r.amount()));
        dst.setBalance(dst.getBalance().add(r.amount()));
        accountRepository.save(src);
        accountRepository.save(dst);
       transactionRepository.save(
                Transaction.builder()
                        .sourceAccountId(src.getId())
                        .destinationAccountId(dst.getId())
                        .amount(r.amount())
                        .createdAt(LocalDateTime.now())
                        .build()
        );
        notificationService.notifyTransfer(src.getId());
    }
}
