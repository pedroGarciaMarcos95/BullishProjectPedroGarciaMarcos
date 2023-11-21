package com.project.bullish.pedro.garcia.product.facade.impl;

import com.project.bullish.pedro.garcia.api.exception.BullishProductException;
import com.project.bullish.pedro.garcia.cart.data.CartData;
import com.project.bullish.pedro.garcia.discount.data.DiscountData;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BullishProductFacadeImplTest {

    private static String CLIENT_ID = "test123";

    @InjectMocks
    private BullishProductFacadeImpl bullishProductFacadeImpl;

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
    void testCreateProduct() {
        when(bullishProductService.createProduct(product1)).thenReturn(product1);

        ProductData productData = bullishProductFacadeImpl.createProduct(product1);
        assertThat(productData, is(product1));
        verify(bullishProductService).createProduct(product1);
    }

    @Test
    void testDeleteProduct() throws BullishProductException {
        bullishProductFacadeImpl.deleteProduct(product1.getId());
        verify(bullishProductService).deleteProduct(product1.getId());
    }

    @Test
    void testGetAllProducts() {
        bullishProductFacadeImpl.getAllProducts();
        verify(bullishProductService).getAllProducts();
    }

    @Test
    void testGetProductById() {
        bullishProductFacadeImpl.getProductById(product1.getId());
        verify(bullishProductService).getProductById(product1.getId());
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