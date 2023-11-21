package com.project.bullish.pedro.garcia.order.facade.impl;

import com.project.bullish.pedro.garcia.order.data.OrderData;
import com.project.bullish.pedro.garcia.order.facade.BullishOrderFacade;
import com.project.bullish.pedro.garcia.order.service.BullishOrderService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BullishOrderFacadeImpl implements BullishOrderFacade {

    @Resource
    private BullishOrderService bullishOrderService;

    @Override
    public Optional<OrderData> createOrderByCartId(String clientId) {
        return Optional.ofNullable(bullishOrderService.createOrderByCartId(clientId));
    }

    @Override
    public Optional<List<OrderData>> getAllOrdersByClientId(String clientId) {
        return Optional.ofNullable(bullishOrderService.getAllOrdersByClientId(clientId));
    }
}
