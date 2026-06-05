
package com.example.bank.dto;

import java.math.BigDecimal;

public record CreateAccountRequest(String name, BigDecimal initialBalance) {
}
