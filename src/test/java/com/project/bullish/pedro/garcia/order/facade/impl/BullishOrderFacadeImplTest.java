package com.project.bullish.pedro.garcia.order.facade.impl;

import com.project.bullish.pedro.garcia.cart.data.CartData;
import com.project.bullish.pedro.garcia.discount.data.DiscountData;
import com.project.bullish.pedro.garcia.order.data.OrderData;
import com.project.bullish.pedro.garcia.order.service.BullishOrderService;
import com.project.bullish.pedro.garcia.product.data.ProductData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BullishOrderFacadeImplTest {

    private static String CLIENT_ID = "test123";

    @InjectMocks
    private BullishOrderFacadeImpl bullishOrderFacadeImpl;

    @Mock
    private BullishOrderService bullishOrderService;

    private CartData cart;
    private ProductData product1;
    private ProductData product2;
    private DiscountData discount;

    private OrderData order;

    private List<ProductData> productDataList;

    @BeforeEach
    void setUp() {
        createProduct();
        createDiscount();
        assingDiscountToProduct(discount, product1);
        productDataList = new ArrayList<>();
        productDataList.add(product1);
        productDataList.add(product2);
        createCart(productDataList);
        createOrder(productDataList);
    }

    @Test
    void testCreateOrderByCartId() {
        when(bullishOrderService.createOrderByCartId(CLIENT_ID)).thenReturn(order);
        Optional<OrderData> orderData = bullishOrderFacadeImpl.createOrderByCartId(CLIENT_ID);
        assertThat(orderData.get(), is(order));
        verify(bullishOrderService).createOrderByCartId(CLIENT_ID);
    }

    @Test
    void testCreateOrderByCartIdWithNullResponseFromOrderService() {
        when(bullishOrderService.createOrderByCartId(CLIENT_ID)).thenReturn(null);
        Optional<OrderData> orderData = bullishOrderFacadeImpl.createOrderByCartId(CLIENT_ID);
        assertThat(orderData, is(Optional.empty()));
        verify(bullishOrderService).createOrderByCartId(CLIENT_ID);
    }

    @Test
    void testGetAllOrdersByClientId() {
        bullishOrderFacadeImpl.getAllOrdersByClientId(CLIENT_ID);
        verify(bullishOrderService).getAllOrdersByClientId(CLIENT_ID);
    }

    @Test
    void testGetAllOrdersByClientIdWithNoOrders() {
        when(bullishOrderService.getAllOrdersByClientId(CLIENT_ID)).thenReturn(null);
        Optional<List<OrderData>> orderDataList = bullishOrderFacadeImpl.getAllOrdersByClientId(CLIENT_ID);
        assertThat(orderDataList, is(Optional.empty()));
        verify(bullishOrderService).getAllOrdersByClientId(CLIENT_ID);
    }

    private void createProduct() {
        product1 = new ProductData();
        product1.setId(1L);
        product1.setPrice(100.0);
        product1.setName("Product 1");

        product2 = new ProductData();
        product2.setId(2L);
        product2.setPrice(20.0);
        product2.setName("Product 2");
    }

    private void createDiscount() {
        discount = new DiscountData();
        discount.setId(1L);
        discount.setPercentage(false);
        discount.setValue(50);
        discount.setMinNumberOfProducts(2);
    }

    private void assingDiscountToProduct(DiscountData discount, ProductData productData) {
        productData.setDiscount(discount);
    }

    private void createCart(List<ProductData> productDataList) {
        cart = new CartData();
        cart.setId(CLIENT_ID);
        cart.setProductDataList(productDataList);
    }

    private void createOrder(List<ProductData> productDataList) {
        order = new OrderData();
        order.setId(CLIENT_ID);
        order.setProductDataList(productDataList);
    }
}