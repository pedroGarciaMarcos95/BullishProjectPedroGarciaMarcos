package com.project.bullish.pedro.garcia.cart.service.impl;

import com.project.bullish.pedro.garcia.cart.data.CartData;
import com.project.bullish.pedro.garcia.discount.data.DiscountData;
import com.project.bullish.pedro.garcia.discount.service.BullishDiscountService;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BullishCartServiceImplTest {

    private static String CLIENT_ID = "test123";

    @InjectMocks
    private BullishCartServiceImpl bullishCartService;

    @Mock
    private BullishDiscountService bullishDiscountService;


    private CartData cart;
    private ProductData product1;
    private ProductData product2;
    private DiscountData discount;

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
    }

    @Test
    void testGetOrCreateCartById() {
        CartData cartData = bullishCartService.getOrCreateCartById(CLIENT_ID);
        assertThat(cartData.getId(), is(CLIENT_ID));
    }

    @Test
    void testGetOrCreateCartByIdWithExistingCartId() {
        bullishCartService.getOrCreateCartById(CLIENT_ID);
        CartData cartData = bullishCartService.getOrCreateCartById(CLIENT_ID);
        assertThat(cartData.getId(), is(CLIENT_ID));
    }

    @Test
    void testGetCartByIdWithNullResult() {
        CartData cartData = bullishCartService.getCartById(CLIENT_ID);
        assertNull(cartData);
    }

    @Test
    void testGetCartById() {
        bullishCartService.getOrCreateCartById(CLIENT_ID);
        CartData cartData = bullishCartService.getCartById(CLIENT_ID);
        assertThat(cartData.getId(), is(CLIENT_ID));
    }

    @Test
    void testAddProductToCart() {
        when(bullishDiscountService.isDiscountApplicable(cart, product1, 2)).thenReturn(true);
        when(bullishDiscountService.isDiscountApplicable(cart, product1, 1)).thenReturn(false);
        when(bullishDiscountService.applyDiscount(cart, product1)).thenReturn(50d);
        bullishCartService.addProductToCart(cart, product1);
        assertThat(cart.getProductDataList().size(), is(3));
        assertNotNull(cart.getTotalAmount());
        assertThat(cart.getTotalAmount(), is(170d));
    }

    @Test
    void testRemoveProductFromCartById() {
        bullishCartService.removeProductFromCartById(cart, product1.getId());
        assertThat(cart.getProductDataList().size(), is(1));
        assertThat(cart.getTotalAmount(), is(20d));
    }

    @Test
    void testCalculateTotalsWithNoDiscountApplied() {
        when(bullishDiscountService.isDiscountApplicable(cart, product1, 1)).thenReturn(false);
        cart.setProductDataList(null);
        bullishCartService.addProductToCart(cart, product1);
        assertThat(cart.getProductDataList().size(), is(1));
        assertNotNull(cart.getTotalAmount());
        assertThat(cart.getTotalAmount(), is(100d));
        verify(bullishDiscountService, never()).applyDiscount(any(), any());
        verify(bullishDiscountService, times(1)).isDiscountApplicable(cart, product1, 1);
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
}