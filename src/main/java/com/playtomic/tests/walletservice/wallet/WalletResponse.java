package com.playtomic.tests.walletservice.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WalletResponse(@JsonProperty String userId, @JsonProperty long balance) {}
