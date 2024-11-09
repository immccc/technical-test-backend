package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.repository.WalletEntity;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.WalletService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    private static final String USER_ID = "an_user_id";
    private static final long WALLET_BALANCE = 100L;

    @Mock
    private WalletRepository repository;

    @InjectMocks
    private WalletService walletService;

    @Test
    @DisplayName("Should retrieve an existing wallet")
    void testGetWallet() {
        when(repository.findById(USER_ID)).thenReturn(
                Optional.of(WalletEntity.builder().userId(USER_ID).balance(WALLET_BALANCE).build())
        );

        var actualWalletResponse = walletService.get(USER_ID).orElseThrow();

        Assertions.assertEquals(USER_ID, actualWalletResponse.userId());
        Assertions.assertEquals(WALLET_BALANCE, actualWalletResponse.balance());
    }

    @Test
    @DisplayName("Should return empty if wallet does not exist")
    void testGetUnexistingWallet() {
        when(repository.findById(USER_ID)).thenReturn(Optional.empty());

        var actualWalletResponse = walletService.get(USER_ID);

        Assertions.assertFalse(actualWalletResponse.isPresent());

    }
}