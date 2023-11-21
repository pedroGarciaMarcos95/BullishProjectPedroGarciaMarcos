package com.project.bullish.pedro.garcia.discount.service.Impl;

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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BullishDiscountServiceImplTest {

    private static String CLIENT_ID = "test123";

    @InjectMocks
    private BullishDiscountServiceImpl bullishDiscountServiceImpl;

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
        productDataList.add(product1);
        productDataList.add(product2);
        createCart(productDataList);
    }

    @Test
    void testCreateDiscount() {
        bullishDiscountServiceImpl.createDiscount(discount);
        assertThat(bullishDiscountServiceImpl.getDiscountById(discount.getId()), is(discount));
    }

    @Test
    void testGetDiscountById() {
        bullishDiscountServiceImpl.createDiscount(discount);
        assertNotNull(bullishDiscountServiceImpl.getDiscountById(discount.getId()));
    }

    @Test
    void testIsDiscountApplicableWithFirstProduct() {
        assertFalse(bullishDiscountServiceImpl.isDiscountApplicable(cart, product1, 1));
        assertFalse(product1.getDiscount().isApplied());
    }

    @Test
    void testIsDiscountApplicableWithSecondProduct() {
        assertTrue(bullishDiscountServiceImpl.isDiscountApplicable(cart, product1, 2));
        assertTrue(product1.getDiscount().isApplied());
    }

    @Test
    void testApplyNonPercentageDiscount() {
        Double discountApplied = bullishDiscountServiceImpl.applyDiscount(cart, product1);
        assertThat(discountApplied, is(product1.getPrice() - product1.getDiscount().getValue()));
    }

    @Test
    void testApplyPercentageDiscount() {
        product1.getDiscount().setPercentage(true);
        Double discountApplied = bullishDiscountServiceImpl.applyDiscount(cart, product1);
        assertThat(discountApplied, is(product1.getPrice() * product1.getDiscount().getValue() / 100));
    }

    @Test
    void testApplyDiscountWithNoDiscountOnProduct() {
        product1.setDiscount(null);
        Double discountApplied = bullishDiscountServiceImpl.applyDiscount(cart, product1);
        assertThat(discountApplied, is(product1.getPrice()));
    }

    private void createProduct() {
        product1 = new ProductData();
        product1.setId(1L);
        product1.setPrice(200.0);
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