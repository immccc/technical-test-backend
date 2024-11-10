package com.playtomic.tests.walletservice.wallet;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
class WalletNotFoundException extends RuntimeException {
    WalletNotFoundException(String userId) {
        super(String.format("Wallet with user id %s not found", userId));
    }
}
