package com.playtomic.tests.wallet.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name="wallets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletEntity {
    @Id
    @Column(name="user_id")
    private String userId;
    private long balance;
}
