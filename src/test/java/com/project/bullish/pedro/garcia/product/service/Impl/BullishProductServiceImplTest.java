package com.project.bullish.pedro.garcia.product.service.Impl;

import com.project.bullish.pedro.garcia.api.exception.BullishProductException;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BullishProductServiceImplTest {

    private static String CLIENT_ID = "test123";

    @InjectMocks
    private BullishProductServiceImpl bullishProductService;

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
        assingDiscountToProduct(discount, product1);
        productDataList = new ArrayList<>();
        productDataList.add(product1);
        productDataList.add(product2);
        createCart(productDataList);
    }

    @Test
    void testCreateProduct() {
        ProductData productData = bullishProductService.createProduct(product1);
        assertThat(productData.getId(), is(1L));
        ProductData productData2 = bullishProductService.createProduct(product2);
        assertThat(productData2.getId(), is(2L));
    }

    @Test
    void testDeleteProduct() throws BullishProductException {
        bullishProductService.deleteProduct(product1.getId());
        verify(bullishCartService).removeProductFromCartIfNeeded(any());
    }

    @Test
    void testGetAllProducts() {
        bullishProductService.createProduct(product1);
        bullishProductService.createProduct(product2);
        List<ProductData> productDataList = bullishProductService.getAllProducts();
        assertThat(productDataList.size(), is(2));
    }

    @Test
    void testGetProductById() {
        bullishProductService.createProduct(product1);
        ProductData productData = bullishProductService.getProductById(product1.getId());
        assertThat(productData, is(product1));
    }

    @Test
    void testGetNonExistingProductById() {
        ProductData productData = bullishProductService.getProductById(product1.getId());
        assertNull(productData);
    }

    @Test
    void testSetDiscountToProduct() {
        bullishProductService.createProduct(product2);

        assertNull(product2.getDiscount());

        bullishProductService.setDiscountToProduct(discount, product2.getId());

        assertThat(product2.getDiscount(), is(discount));
    }

    @Test
    void testSetDiscountToNonExistingProduct() {
        bullishProductService.setDiscountToProduct(discount, product2.getId());

        assertNull(product2.getDiscount());
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