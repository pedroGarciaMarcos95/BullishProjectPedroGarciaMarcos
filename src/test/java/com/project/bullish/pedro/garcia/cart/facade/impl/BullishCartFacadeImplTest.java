package com.project.bullish.pedro.garcia.cart.facade.impl;

import com.project.bullish.pedro.garcia.api.exception.BullishNullCartException;
import com.project.bullish.pedro.garcia.cart.data.CartData;
import com.project.bullish.pedro.garcia.cart.service.BullishCartService;
import com.project.bullish.pedro.garcia.discount.data.DiscountData;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BullishCartFacadeImplTest {

    private static String CLIENT_ID = "test123";

    @InjectMocks
    private BullishCartFacadeImpl bullishCartFacade;

    @Mock
    private BullishCartService bullishCartService;

    private CartData cart;
    private ProductData product1;
    private ProductData product2;
    private DiscountData discount;

    private List<ProductData> productDataList;

    @BeforeEach
    void setUp() {
        createProduct();
        createDiscount();
        assingDiscountToProdduct(discount, product1);
        productDataList = new ArrayList<>();
        productDataList.add(product1);
        productDataList.add(product2);
        createCart(productDataList);
    }

    @Test
    void testGetOrCreateCartById() {
        bullishCartFacade.getOrCreateCartById(CLIENT_ID);
        verify(bullishCartService).getOrCreateCartById(CLIENT_ID);
    }

    @Test
    void testGetCartById() {
        bullishCartFacade.getCartById(CLIENT_ID);
        verify(bullishCartService).getCartById(CLIENT_ID);
    }

    @Test
    void testAddProductToCart() throws BullishNullCartException {
        bullishCartFacade.addProductToCart(cart, product1);
        verify(bullishCartService).addProductToCart(cart, product1);
    }

    @Test
    void testAddProductToNullCart() {
        assertThrows(BullishNullCartException.class, () -> bullishCartFacade.addProductToCart(null, product1));
    }

    @Test
    void testRemoveProductFromCartById() {
        when(bullishCartService.getCartById(CLIENT_ID)).thenReturn(cart);
        bullishCartFacade.removeProductFromCartById(cart.getId(), product1.getId());
        verify(bullishCartService).removeProductFromCartById(cart, product1.getId());
        verify(bullishCartService).getCartById(cart.getId());
    }

    @Test
    void testRemoveProductFromCartByIdWithNonExistingCart() throws BullishNullCartException {
        when(bullishCartService.getCartById(cart.getId())).thenReturn(null);

        Optional<CartData> cartData = bullishCartFacade.removeProductFromCartById(cart.getId(), product1.getId());
        verify(bullishCartService).getCartById(cart.getId());
        assertThat(cartData, is(Optional.empty()));
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

    private void assingDiscountToProdduct(DiscountData discount, ProductData productData) {
        productData.setDiscount(discount);
    }

    private void createCart(List<ProductData> productDataList) {
        cart = new CartData();
        cart.setId(CLIENT_ID);
        cart.setProductDataList(productDataList);
    }
}