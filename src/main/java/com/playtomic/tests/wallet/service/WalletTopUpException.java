package com.playtomic.tests.wallet.service;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class WalletTopUpException extends RuntimeException {
    public WalletTopUpException(String userId, long amount) {
        super(String.format("Unable to topup for user %s amount %d", userId, amount));
    }
}
