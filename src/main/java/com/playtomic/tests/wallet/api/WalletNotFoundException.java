package com.playtomic.tests.wallet.api;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException(String userId) {
        super(String.format("Wallet with user id %s not found", userId));
    }
}
