package com.playtomic.tests.walletservice.wallet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("wallets")
@Slf4j
public record WalletController(WalletService walletService) {

    @GetMapping("{userId}")
    public WalletResponse get(@PathVariable String userId) {
        return walletService.get(userId);
    }

    @PostMapping
    public void topUp(@RequestBody WalletTopUpRequest walletTopUpRequest) {
        walletService.topUp(
                walletTopUpRequest.userId(),
                walletTopUpRequest.creditCard(),
                walletTopUpRequest.topUpAmount()
        );
    }

}
