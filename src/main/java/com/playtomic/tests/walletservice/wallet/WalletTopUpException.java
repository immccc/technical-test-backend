package com.playtomic.tests.walletservice.wallet;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
class WalletTopUpException extends RuntimeException {
    WalletTopUpException(String userId, long amount) {
        super(String.format("Unable to topup for user %s amount %d", userId, amount));
    }
}
