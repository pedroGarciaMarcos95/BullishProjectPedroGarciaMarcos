package com.project.bullish.pedro.garcia.order.service.impl;

import com.project.bullish.pedro.garcia.cart.data.CartData;
import com.project.bullish.pedro.garcia.cart.service.BullishCartService;
import com.project.bullish.pedro.garcia.order.data.OrderData;
import com.project.bullish.pedro.garcia.order.service.BullishOrderService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BullishOrderServiceImpl implements BullishOrderService {

    private final Map<String, List<OrderData>> orders = new HashMap<>();

    @Resource
    private BullishCartService bullishCartService;

    @Override
    public OrderData createOrderByCartId(String clientId) {
        CartData cart = bullishCartService.getCartById(clientId);

        if (cart != null && !cart.getProductDataList().isEmpty()) {
            OrderData order = new OrderData(cart);
            List<OrderData> clientOrders = orders.computeIfAbsent(clientId, k -> new ArrayList<>());
            clientOrders.add(order);
            bullishCartService.clearCart(clientId);
            return order;
        } else {
            return null;
        }
    }

    @Override
    public List<OrderData> getAllOrdersByClientId(String clientId) {
        return orders.get(clientId) != null ? orders.get(clientId) : null;
    }

}
