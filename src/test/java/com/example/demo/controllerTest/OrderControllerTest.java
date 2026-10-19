package com.example.demo.controllerTest;

import com.example.demo.controller.OrderController;
import com.example.demo.dto.OrderResponse;
import com.example.demo.ratelim.RateLimitFilter;
import com.example.demo.service.OrderService;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = RateLimitFilter.class))
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Test
    void shouldCreateOrder() throws Exception {
        OrderResponse response = new OrderResponse(
            1L, "Rahul", new BigDecimal("2500.00"));

        when(orderService.createOrder(any(), eq("order-1001")))
            .thenReturn(response);

        mockMvc.perform(post("/orders")
                .header("Idempotency-Key", "order-1001")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "customerName": "Rahul",
                        "amount": 2500.00
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.customerName").value("Rahul"))
            .andExpect(jsonPath("$.amount").value(2500.00));
    }
}
