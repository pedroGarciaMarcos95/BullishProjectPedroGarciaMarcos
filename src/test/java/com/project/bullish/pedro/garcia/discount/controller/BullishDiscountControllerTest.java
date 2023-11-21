package com.project.bullish.pedro.garcia.discount.controller;

import com.project.bullish.pedro.garcia.discount.data.DiscountData;
import com.project.bullish.pedro.garcia.product.data.ProductData;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(loader = SpringBootContextLoader.class)
class BullishDiscountControllerTest {

    private static String USER = "admin";
    private static String PASSWORD = "nimda";

    @Resource
    private MockMvc mockMvc;

    @Resource
    private ObjectMapper objectMapper;

    private DiscountData discount;
    private ProductData product1;

    @BeforeEach
    void setUp() {
        createDiscount();
        createProduct();
    }

    @Test
    public void testDiscountController() throws Exception {
        testCreateDiscount();
        testAssignDiscountToProduct();
        testCreateAndAssignDiscountToProduct();
    }

    public void testCreateDiscount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/discount/create")
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(USER, PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(discount)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.percentage").value(discount.isPercentage()))
                .andExpect(jsonPath("$.minNumberOfProducts").value(discount.getMinNumberOfProducts()))
                .andExpect(jsonPath("$.value").value(discount.getValue()));
    }

    public void testAssignDiscountToProduct() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product/addNewProduct")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(USER, PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product1)));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/discount/assign/{productId}/{discountId}",
                                discount.getId(), discount.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(USER, PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.discount.id").value(discount.getId()));
    }

    public void testCreateAndAssignDiscountToProduct() throws Exception {


        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/discount/create-and-assign/{productId}",
                                product1.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(USER, PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(discount)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(product1.getId()))
                .andExpect(jsonPath("$.name").value(product1.getName()))
                .andExpect(jsonPath("$.price").value(product1.getPrice()))
                .andExpect(jsonPath("$.discount.percentage").value(discount.isPercentage()))
                .andExpect(jsonPath("$.discount.id").value(2L)) //It´s the second discount that has been created so the ID is always 2
                .andExpect(jsonPath("$.discount.minNumberOfProducts").value(discount.getMinNumberOfProducts()))
                .andExpect(jsonPath("$.discount.value").value(discount.getValue()));
    }

    private String getBase64Credentials(String username, String password) {
        String credentials = username + ":" + password;
        return new String(java.util.Base64.getEncoder().encode(credentials.getBytes()));
    }

    private void createDiscount() {
        discount = new DiscountData();
        discount.setId(1L);
        discount.setPercentage(false);
        discount.setValue(50);
        discount.setMinNumberOfProducts(2);
    }

    private void createProduct() {
        product1 = new ProductData();
        product1.setId(1L);
        product1.setPrice(100.0);
        product1.setName("Product 1");
    }
}