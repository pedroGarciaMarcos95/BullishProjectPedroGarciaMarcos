package com.project.bullish.pedro.garcia.order.service.impl;

import com.project.bullish.pedro.garcia.cart.data.CartData;
import com.project.bullish.pedro.garcia.cart.service.BullishCartService;
import com.project.bullish.pedro.garcia.discount.data.DiscountData;
import com.project.bullish.pedro.garcia.order.data.OrderData;
import com.project.bullish.pedro.garcia.product.data.ProductData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BullishOrderServiceImplTest {

    private static String CLIENT_ID = "test123";

    @InjectMocks
    private BullishOrderServiceImpl bullishOrderServiceImpl;

    @Mock
    private BullishCartService bullishCartService;

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
        when(bullishCartService.getCartById(CLIENT_ID)).thenReturn(cart);
        OrderData orderData = bullishOrderServiceImpl.createOrderByCartId(CLIENT_ID);
        assertThat(orderData.getId(), is(cart.getId()));
        assertThat(orderData.getProductDataList(), is(cart.getProductDataList()));
        verify(bullishCartService).clearCart(CLIENT_ID);
        verify(bullishCartService).getCartById(CLIENT_ID);
    }

    @Test
    void testCreateOrderByNullCartId() {
        when(bullishCartService.getCartById(CLIENT_ID)).thenReturn(null);
        OrderData orderData = bullishOrderServiceImpl.createOrderByCartId(CLIENT_ID);
        verify(bullishCartService).getCartById(CLIENT_ID);
        assertNull(orderData);
    }

    @Test
    void testGetAllOrdersByClientId() {
        when(bullishCartService.getCartById(CLIENT_ID)).thenReturn(cart);
        bullishOrderServiceImpl.createOrderByCartId(CLIENT_ID);
        bullishOrderServiceImpl.createOrderByCartId(CLIENT_ID);
        List<OrderData> orderDataList = bullishOrderServiceImpl.getAllOrdersByClientId(CLIENT_ID);
        assertThat(orderDataList.size(), is(2));
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