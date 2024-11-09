package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public record WalletController(WalletService walletService) {

    // TODO Looks like a health endpoint for the service. Perhaps should be moved out to its own controller.
    @RequestMapping("/")
    public void log() {
        log.info("Logging from /");
    }

    @GetMapping("wallets/{userId}")
    public WalletResponse get(@PathVariable String userId) {

        return walletService.get(userId).orElseThrow(() -> new WalletNotFoundException(userId));
    }

}
