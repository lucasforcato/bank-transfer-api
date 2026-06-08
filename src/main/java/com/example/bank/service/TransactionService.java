package com.example.bank.service;

import com.example.bank.dto.TransactionResponse;
import com.example.bank.entity.Transaction;
import com.example.bank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public List<TransactionResponse> findAll() {

        return transactionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TransactionResponse> findByAccountId(Long accountId) {

        return transactionRepository
                .findBySourceAccountIdOrDestinationAccountId(
                        accountId,
                        accountId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private TransactionResponse toResponse(Transaction transaction) {

        return new TransactionResponse(
                transaction.getId(),
                transaction.getSourceAccountId(),
                transaction.getDestinationAccountId(),
                transaction.getAmount(),
                transaction.getCreatedAt()
        );
    }
}