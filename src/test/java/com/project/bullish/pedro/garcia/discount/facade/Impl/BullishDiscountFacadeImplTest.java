package com.project.bullish.pedro.garcia.discount.facade.Impl;

import com.project.bullish.pedro.garcia.api.exception.BullishDiscountNotFoundException;
import com.project.bullish.pedro.garcia.cart.data.CartData;
import com.project.bullish.pedro.garcia.discount.data.DiscountData;
import com.project.bullish.pedro.garcia.discount.service.BullishDiscountService;
import com.project.bullish.pedro.garcia.product.data.ProductData;
import com.project.bullish.pedro.garcia.product.service.BullishProductService;
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
class BullishDiscountFacadeImplTest {

    private static String CLIENT_ID = "test123";

    @InjectMocks
    private BullishDiscountFacadeImpl bullishDiscountFacadeImpl;

    @Mock
    private BullishDiscountService bullishDiscountService;

    @Mock
    private BullishProductService bullishProductService;

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
    void testCreateDiscount() {
        bullishDiscountFacadeImpl.createDiscount(discount);
        verify(bullishDiscountService).createDiscount(discount);
    }

    @Test
    void testAssignDiscountToProduct() {
        when(bullishDiscountService.getDiscountById(discount.getId())).thenReturn(discount);
        when(bullishProductService.setDiscountToProduct(discount, product1.getId())).thenReturn(product1);

        Optional<ProductData> productData = bullishDiscountFacadeImpl.assignDiscountToProduct(product1.getId(), discount.getId());
        assertThat(productData.get().getDiscount().getId(), is(discount.getId()));
    }

    @Test
    void testAssignDiscountToProductWithNoDiscountFound() {
        when(bullishDiscountService.getDiscountById(discount.getId())).thenReturn(null);

        assertThrows(BullishDiscountNotFoundException.class, () -> bullishDiscountFacadeImpl.assignDiscountToProduct(product1.getId(), discount.getId()));
    }

    @Test
    void testCreateAndAssignDiscountToProduct() {
        when(bullishDiscountService.createDiscount(discount)).thenReturn(discount);
        when(bullishDiscountService.getDiscountById(discount.getId())).thenReturn(discount);
        when(bullishProductService.setDiscountToProduct(discount, product1.getId())).thenReturn(product1);

        Optional<ProductData> productData = bullishDiscountFacadeImpl.createAndAssignDiscountToProduct(discount, product1.getId());
        assertThat(productData.get().getDiscount().getId(), is(discount.getId()));
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