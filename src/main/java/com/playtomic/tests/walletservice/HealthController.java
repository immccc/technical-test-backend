package com.playtomic.tests.walletservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Slf4j
public class HealthController {

    @RequestMapping
    public void log() {
        log.info("Logging from /");
    }
}
