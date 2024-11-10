package com.playtomic.tests.walletservice.wallet;

import com.playtomic.tests.walletservice.payments.stripe.StripeService;
import com.playtomic.tests.walletservice.payments.stripe.StripeServiceException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
class WalletService {

    private static final int DEFAULT_CENT_DIGITS = 2;

    StripeService stripeService;
    WalletRepository repository;

    WalletResponse get(String userId) {
        return repository.findById(userId)
                .map(entity -> new WalletResponse(entity.getUserId(), entity.getBalance()))
                .orElseThrow(() -> new WalletNotFoundException(userId));
    }

    @Transactional
    synchronized void topUp(String userId, String creditCard, long amount) {
        var walletEntity = repository.findById(userId).orElseThrow(() -> new WalletNotFoundException(userId));
        try {
            stripeService.charge(creditCard, BigDecimal.valueOf(amount, DEFAULT_CENT_DIGITS));
            walletEntity.setBalance(walletEntity.getBalance() + amount);
        } catch (StripeServiceException e) {
            throw new WalletTopUpException(userId, amount);
        }
    }
}
