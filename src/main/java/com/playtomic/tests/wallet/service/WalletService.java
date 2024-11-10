package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.api.WalletResponse;
import com.playtomic.tests.wallet.repository.WalletRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class WalletService {

    private static final int DEFAULT_CENT_DIGITS = 2;

    StripeService stripeService;
    WalletRepository repository;

    public Optional<WalletResponse> get(String userId) {
        return repository.findById(userId)
                .map(entity -> new WalletResponse(entity.getUserId(), entity.getBalance()));
    }

    @Transactional
    public synchronized void topUp(String userId, String creditCard, long amount) {
        var walletEntity = repository.findById(userId).orElseThrow(() -> new WalletNotFoundException(userId));
        try {
            stripeService.charge(creditCard, BigDecimal.valueOf(amount, DEFAULT_CENT_DIGITS));
            walletEntity.setBalance(walletEntity.getBalance() + amount);
        } catch (StripeServiceException e) {
            throw new WalletTopUpException(userId, amount);
        }
    }
}
