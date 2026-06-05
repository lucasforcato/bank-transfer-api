
package com.example.bank.dto;

import java.math.BigDecimal;

public record TransferRequest(Long sourceAccountId, Long destinationAccountId, BigDecimal amount) {
}
