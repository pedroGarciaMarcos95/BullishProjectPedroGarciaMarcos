package com.project.bullish.pedro.garcia.order.facade;

import com.project.bullish.pedro.garcia.api.exception.BullishNullCartException;
import com.project.bullish.pedro.garcia.order.data.OrderData;

import java.util.List;
import java.util.Optional;

public interface BullishOrderFacade {

    Optional<OrderData> createOrderByCartId(String clientId) throws BullishNullCartException;

    Optional<List<OrderData>> getAllOrdersByClientId(String clientId);

}
