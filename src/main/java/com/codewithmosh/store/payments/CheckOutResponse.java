package com.codewithmosh.store.payments;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckOutResponse {
    private Long orderId;
    private String checkoutUrl;
}
