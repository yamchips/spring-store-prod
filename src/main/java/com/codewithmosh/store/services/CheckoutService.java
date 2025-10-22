package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.CheckOutRequest;
import com.codewithmosh.store.dtos.CheckOutResponse;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.entities.PaymentStatus;
import com.codewithmosh.store.exceptions.CartEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.PaymentException;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.OrderRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;

    @Transactional
    public CheckOutResponse checkout(CheckOutRequest request) {
        Cart cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        if (cart.isEmpty()) {
            throw new CartEmptyException();
        }
        var order = Order.fromCart(cart, authService.getCurrentUser());
        orderRepository.save(order);

        try {
            var session = paymentGateway.createCheckoutSession(order);
            cartService.clearCart(request.getCartId());
            return new CheckOutResponse(order.getId(), session.getCheckoutUrl());
        } catch (PaymentException e) {
            orderRepository.delete(order);
            throw e;
        }

    }

    public void handleWebhookRequest(WebhookRequest webhookRequest) {
        paymentGateway
                .parseWebhookRequest(webhookRequest)
                .ifPresent(paymentResult -> {
                    Order order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
                    order.setStatus(paymentResult.getPaymentStatus());
                    orderRepository.save(order);
                });
    }
}
