package com.project.bullish.pedro.garcia.order.controller;

import com.project.bullish.pedro.garcia.api.exception.BullishNullCartException;
import com.project.bullish.pedro.garcia.order.data.OrderData;
import com.project.bullish.pedro.garcia.order.facade.BullishOrderFacade;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class BullishOrderController {

    @Resource
    private BullishOrderFacade bullishOrderFacade;

    @PostMapping("/purchaseCart/{clientId}")
    public ResponseEntity<OrderData> purchaseCart(
            @PathVariable final String clientId) throws BullishNullCartException {
        return new ResponseEntity<>(bullishOrderFacade.createOrderByCartId(clientId)
                .orElseThrow(() -> new BullishNullCartException("Cart not found for ID: " + clientId)), HttpStatus.OK);
    }

    @GetMapping("/getAllOrdersByClientId/{clientId}")
    public ResponseEntity<List<OrderData>> getAllOrdersByClientId(
            @PathVariable final String clientId) throws BullishNullCartException {
        return new ResponseEntity<>(bullishOrderFacade.getAllOrdersByClientId(clientId)
                .orElseThrow(() -> new BullishNullCartException("Order not found for ID: " + clientId)), HttpStatus.OK);
    }
}
