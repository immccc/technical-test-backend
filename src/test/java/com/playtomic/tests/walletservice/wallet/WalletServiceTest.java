package com.playtomic.tests.walletservice.wallet;

import com.playtomic.tests.walletservice.payments.stripe.StripeService;
import com.playtomic.tests.walletservice.payments.stripe.StripeServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    private static final String USER_ID = "an_user_id";
    private static final long WALLET_BALANCE = 100L;

    @Mock
    private StripeService stripeService;

    @Mock
    private WalletRepository repository;

    @InjectMocks
    private WalletService walletService;

    @Nested
    @DisplayName("Retrieval of wallet")
    class Get {
        @Test
        @DisplayName("Should retrieve an existing wallet")
        void testGetWallet() {
            when(repository.findById(USER_ID)).thenReturn(
                    Optional.of(WalletEntity.builder().userId(USER_ID).balance(WALLET_BALANCE).build())
            );

            var actualWalletResponse = walletService.get(USER_ID);

            Assertions.assertEquals(USER_ID, actualWalletResponse.userId());
            Assertions.assertEquals(WALLET_BALANCE, actualWalletResponse.balance());
        }

        @Test
        @DisplayName("Should return empty if wallet does not exist")
        void testGetUnexistingWallet() {
            when(repository.findById(USER_ID)).thenReturn(Optional.empty());
            Assertions.assertThrows(WalletNotFoundException.class, () -> walletService.get(USER_ID));
        }
    }

    @Nested
    @DisplayName("Top-up of a wallet")
    class TopUp {
        public static final long TOP_UP_AMOUNT = 100L;
        public static final String CREDIT_CARD = "2222 2222 2222 2222";

        @Test
        @DisplayName("Should top up an existing balance")
        public void testTopUpWallet() {
            var walletEntityToUpdate = WalletEntity.builder().userId(USER_ID).balance(WALLET_BALANCE).build();
            when(repository.findById(USER_ID)).thenReturn(
                    Optional.of(walletEntityToUpdate)
            );

            walletService.topUp(USER_ID, CREDIT_CARD, TOP_UP_AMOUNT);
            verify(stripeService).charge(CREDIT_CARD, BigDecimal.valueOf(TOP_UP_AMOUNT, 2));

            Assertions.assertEquals(WALLET_BALANCE + TOP_UP_AMOUNT, walletEntityToUpdate.getBalance());

        }

        @Test
        @DisplayName("Should fail if wallet does not exist")
        public void testTopUpNonExistingWallet() {
            Assertions.assertThrows(
                    WalletNotFoundException.class, () -> walletService.topUp(USER_ID, CREDIT_CARD, TOP_UP_AMOUNT));
        }

        @Test
        @DisplayName("Should fail if provider throws an error")
        public void testTopUpOutOfFundsWallet() {
            when(repository.findById(USER_ID)).thenReturn(
                    Optional.of(WalletEntity.builder().userId(USER_ID).balance(WALLET_BALANCE).build())
            );
            doThrow(StripeServiceException.class).when(stripeService).charge(eq(CREDIT_CARD), any(BigDecimal.class));

            Assertions.assertThrows(WalletTopUpException.class,
                    () -> walletService.topUp(USER_ID, CREDIT_CARD, TOP_UP_AMOUNT));
        }
    }

}