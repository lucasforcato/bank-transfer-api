
package com.example.bank.controller;

import com.example.bank.dto.TransferRequest;
import com.example.bank.dto.TransferResponse;
import com.example.bank.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<TransferResponse> transfer(
            @RequestBody TransferRequest request) {

        transferService.transfer(request);

        return ResponseEntity.ok(
                new TransferResponse(
                        "Transfer completed successfully"
                )
        );
    }
}