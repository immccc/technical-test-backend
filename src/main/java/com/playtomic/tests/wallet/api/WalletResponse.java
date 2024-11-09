package com.playtomic.tests.wallet.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WalletResponse(@JsonProperty String userId, @JsonProperty long balance) {}
