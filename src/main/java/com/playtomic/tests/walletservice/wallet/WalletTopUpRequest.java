package com.playtomic.tests.walletservice.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WalletTopUpRequest(
        @JsonProperty String userId,
        @JsonProperty String creditCard,
        @JsonProperty long topUpAmount
) {}
