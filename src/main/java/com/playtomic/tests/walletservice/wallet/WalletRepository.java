package com.playtomic.tests.walletservice.wallet;

import org.springframework.data.repository.CrudRepository;

interface WalletRepository extends CrudRepository<WalletEntity, String> {
}
