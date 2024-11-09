package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.api.WalletResponse;
import com.playtomic.tests.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public record WalletService(WalletRepository repository) {

    public Optional<WalletResponse> get(String userId) {
        return repository.findById(userId)
                .map(entity -> new WalletResponse(entity.getUserId(), entity.getBalance()));
    }
}
