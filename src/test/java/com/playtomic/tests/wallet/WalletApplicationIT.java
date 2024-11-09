package com.playtomic.tests.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.api.WalletResponse;
import com.playtomic.tests.wallet.repository.WalletEntity;
import com.playtomic.tests.wallet.repository.WalletRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
public class WalletApplicationIT {

    private static final String USER_ID = "an_user_id";
    private static final long WALLET_BALANCE = 10L;

    @Autowired
    private WalletRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should retrieve a wallet")
    @SneakyThrows
    public void testGetWallet() {
        repository.save(WalletEntity.builder()
                .userId(USER_ID)
                .balance(WALLET_BALANCE)
                .build());

        var result = mockMvc.perform(get("/wallets/" + USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();


        var actualResponse = objectMapper.readValue(result.getResponse().getContentAsString(), WalletResponse.class);
        Assertions.assertEquals(USER_ID, actualResponse.userId());
        Assertions.assertEquals(WALLET_BALANCE, actualResponse.balance());

    }

    @Test
    @DisplayName("Should return 404 when wallet does not exist")
    @SneakyThrows
    public void testGetNonExistingWallet() {
        mockMvc.perform(get("/wallets/" + USER_ID))
                .andExpect(status().isNotFound());
    }
}
