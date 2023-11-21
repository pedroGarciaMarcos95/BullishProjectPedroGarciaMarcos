package com.project.bullish.pedro.garcia.order.service;

import com.project.bullish.pedro.garcia.order.data.OrderData;

import java.util.List;

public interface BullishOrderService {

    OrderData createOrderByCartId(String clientId);

    List<OrderData> getAllOrdersByClientId(String clientId);

}
