package com.example.bank;

import com.example.bank.dto.TransactionResponse;
import com.example.bank.dto.TransferRequest;
import com.example.bank.entity.Account;
import com.example.bank.entity.Transaction;
import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.exception.InsufficientBalanceException;
import com.example.bank.exception.InvalidTransferException;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.TransactionRepository;
import com.example.bank.service.NotificationService;
import com.example.bank.service.TransactionService;
import com.example.bank.service.TransferService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferServiceTest {

    @Test
    void shouldTransfer() {

        AccountRepository ar = mock(AccountRepository.class);
        TransactionRepository tr = mock(TransactionRepository.class);
        NotificationService ns = mock(NotificationService.class);

        TransferService service =
                new TransferService(ar, tr, ns);

        Account source = new Account();
        source.setId(1L);
        source.setBalance(new BigDecimal("100"));

        Account destination = new Account();
        destination.setId(2L);
        destination.setBalance(new BigDecimal("50"));

        when(ar.findById(1L))
                .thenReturn(Optional.of(source));

        when(ar.findById(2L))
                .thenReturn(Optional.of(destination));

        service.transfer(
                new TransferRequest(
                        1L,
                        2L,
                        new BigDecimal("10")
                )
        );

        verify(ns).notifyTransfer(1L);
    }

    @Test
    void shouldThrowExceptionWhenBalanceIsInsufficient() {

        AccountRepository ar = mock(AccountRepository.class);
        TransactionRepository tr = mock(TransactionRepository.class);
        NotificationService ns = mock(NotificationService.class);

        TransferService service =
                new TransferService(ar, tr, ns);

        Account source = new Account();
        source.setId(1L);
        source.setBalance(new BigDecimal("50"));

        Account destination = new Account();
        destination.setId(2L);
        destination.setBalance(new BigDecimal("100"));

        when(ar.findById(1L))
                .thenReturn(Optional.of(source));

        when(ar.findById(2L))
                .thenReturn(Optional.of(destination));

        assertThrows(
                InsufficientBalanceException.class,
                () -> service.transfer(
                        new TransferRequest(
                                1L,
                                2L,
                                new BigDecimal("100")
                        )
                )
        );

        verify(tr, never()).save(any());
        verify(ns, never()).notifyTransfer(anyLong());
    }

    @Test
    void shouldThrowExceptionWhenAccountDoesNotExist() {

        AccountRepository ar = mock(AccountRepository.class);
        TransactionRepository tr = mock(TransactionRepository.class);
        NotificationService ns = mock(NotificationService.class);

        TransferService service =
                new TransferService(ar, tr, ns);

        when(ar.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                AccountNotFoundException.class,
                () -> service.transfer(
                        new TransferRequest(
                                999L,
                                2L,
                                new BigDecimal("10")
                        )
                )
        );

        verify(tr, never()).save(any());
        verify(ns, never()).notifyTransfer(anyLong());
    }

    @Test
    void shouldReturnTransactionsByAccountId() {

        TransactionRepository repository =
                mock(TransactionRepository.class);

        TransactionService service =
                new TransactionService(repository);

        Transaction transaction = Transaction.builder()
                .id(1L)
                .sourceAccountId(1L)
                .destinationAccountId(2L)
                .amount(new BigDecimal("100"))
                .build();

        when(repository
                .findBySourceAccountIdOrDestinationAccountId(
                        1L,
                        1L))
                .thenReturn(List.of(transaction));

        List<TransactionResponse> result =
                service.findByAccountId(1L);

        assertEquals(1, result.size());

        assertEquals(
                1L,
                result.get(0).sourceAccountId()
        );

        verify(repository)
                .findBySourceAccountIdOrDestinationAccountId(
                        1L,
                        1L
                );
    }

    @Test
    void shouldReturnEmptyListWhenAccountHasNoTransactions() {

        TransactionRepository repository =
                mock(TransactionRepository.class);

        TransactionService service =
                new TransactionService(repository);

        when(repository
                .findBySourceAccountIdOrDestinationAccountId(
                        1L,
                        1L))
                .thenReturn(List.of());

        List<TransactionResponse> result =
                service.findByAccountId(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnAllTransactions() {

        TransactionRepository repository =
                mock(TransactionRepository.class);

        TransactionService service =
                new TransactionService(repository);

        Transaction t1 = Transaction.builder()
                .id(1L)
                .build();

        Transaction t2 = Transaction.builder()
                .id(2L)
                .build();

        when(repository.findAll())
                .thenReturn(List.of(t1, t2));

        List<TransactionResponse> result =
                service.findAll();

        assertEquals(2, result.size());

        verify(repository).findAll();
    }
}