
package com.example.bank.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.*;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {

    public void notifyTransfer(Long accountId) {
        log.info("Transfer completed for account {}", accountId);
    }
}
