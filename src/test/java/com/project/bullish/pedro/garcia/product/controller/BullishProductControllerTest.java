package com.project.bullish.pedro.garcia.product.controller;

import com.project.bullish.pedro.garcia.api.exception.BullishProductException;
import com.project.bullish.pedro.garcia.discount.data.DiscountData;
import com.project.bullish.pedro.garcia.product.data.ProductData;
import com.project.bullish.pedro.garcia.product.facade.BullishProductFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(loader = SpringBootContextLoader.class)
class BullishProductControllerTest {

    private static String USER = "admin";
    private static String PASSWORD = "nimda";

    @Resource
    private MockMvc mockMvc;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private BullishProductFacade bullishProductFacade;

    private ProductData product1;
    private ProductData product2;
    private DiscountData discount;

    @BeforeEach
    void setUp() {
        createProduct();
        createDiscount();
        assingDiscountToProduct(discount, product1);
    }

    @Test
    public void testCreateProduct() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product/addNewProduct")
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(USER, PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product1)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.name").value(product1.getName()))
                .andExpect(jsonPath("$.price").value(product1.getPrice()))
                .andExpect(jsonPath("$.discount.percentage").value(discount.isPercentage()))
                .andExpect(jsonPath("$.discount.id").value(discount.getId()))
                .andExpect(jsonPath("$.discount.minNumberOfProducts").value(discount.getMinNumberOfProducts()))
                .andExpect(jsonPath("$.discount.value").value(discount.getValue()));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product/addNewProduct")
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(USER, PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product2)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.name").value(product2.getName()))
                .andExpect(jsonPath("$.price").value(product2.getPrice()))
                .andExpect(jsonPath("$.discount").doesNotExist());

        //I will remove the product as I can not use the @Transactional annotation.
        //As I´m trying to get all the products on the #testGetAllProducts method, if I run it independently it works,
        //but if I run it with the complete test I get an error because these two products are created and the size of the list does not match.
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/product/remove/{productId}", product1.getId())
                .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(USER, PASSWORD)));
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/product/remove/{productId}", product2.getId())
                .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(USER, PASSWORD)));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        ProductData addedProduct = bullishProductFacade.createProduct(product1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/product/remove/{productId}", addedProduct.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(USER, PASSWORD)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Rollback
    public void testGetAllProducts() throws Exception {

        //Here we remove all the products directly in order to test the get all productList
        List<ProductData> productDataList = bullishProductFacade.getAllProducts();
        productDataList.forEach(productData -> {
            try {
                bullishProductFacade.deleteProduct(productData.getId());
            } catch (BullishProductException e) {
                throw new RuntimeException(e);
            }
        });

        bullishProductFacade.createProduct(product1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/product/get/allProducts")
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(USER, PASSWORD)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0].name").value(product1.getName()))
                .andExpect(jsonPath("$[0].price").value(product1.getPrice()))
                .andExpect(jsonPath("$[0].discount.percentage").value(discount.isPercentage()))
                .andExpect(jsonPath("$[0].discount.minNumberOfProducts").value(discount.getMinNumberOfProducts()))
                .andExpect(jsonPath("$[0].discount.value").value(discount.getValue()));

    }

    private String getBase64Credentials(String username, String password) {
        String credentials = username + ":" + password;
        return new String(java.util.Base64.getEncoder().encode(credentials.getBytes()));
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

}