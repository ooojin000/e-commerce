package com.woojin.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("주문을 생성한다")
    void createOrder_success() throws Exception {
        // given
        String body = objectMapper.writeValueAsString(Map.of(
                "userId", 2L,
                "productId", 1L,
                "quantity", 1
        ));

        // when & then
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("주문 내역을 조회한다")
    void getOrderList_success() throws Exception {
        // given: 주문 하나 만들고
        String body = objectMapper.writeValueAsString(Map.of(
                "userId", 2L,
                "productId", 1L,
                "quantity", 1
        ));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        // when & then: 조회
        mockMvc.perform(get("/api/users/{userId}/orders", 2L)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray());
    }

    @Test
    @DisplayName("주문을 취소한다")
    void cancelOrder_success() throws Exception {
        // given
        String body = objectMapper.writeValueAsString(Map.of(
                "userId", 2L,
                "productId", 1L,
                "quantity", 1
        ));

        String res = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long orderId = objectMapper.readTree(res).path("data").path("orderId").asLong();

        // when & then
        mockMvc.perform(patch("/api/orders/{orderId}/cancel", orderId)
                        .header("USER-ID", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
