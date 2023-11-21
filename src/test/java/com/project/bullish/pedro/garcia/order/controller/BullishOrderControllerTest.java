package com.project.bullish.pedro.garcia.order.controller;

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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(loader = SpringBootContextLoader.class)
class BullishOrderControllerTest {

    private static String ADMIN_USER = "admin";
    private static String ADMIN_PASSWORD = "nimda";
    private static String USER = "user";
    private static String USER_PASSWORD = "password";
    private static final String CLIENT_ID = "testClientId";

    @Resource
    private MockMvc mockMvc;

    @Resource
    private ObjectMapper objectMapper;

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
    public void testOrderController() throws Exception {
        testPurchaseCart();
        testGetAllOrdersByClientId();
    }

    public void testPurchaseCart() throws Exception {

        ResultActions resultActionsOrder = createOrderForCustomer(CLIENT_ID);
        resultActionsOrder
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.totalAmmount").value(product1.getPrice()))
                .andExpect(jsonPath("$.productDataList[0].id").value(product1.getId()))
                .andExpect(jsonPath("$.productDataList[0].name").value(product1.getName()))
                .andExpect(jsonPath("$.productDataList[0].price").value(product1.getPrice()))
                .andExpect(jsonPath("$.productDataList[0].discount.percentage").value(discount.isPercentage()))
                .andExpect(jsonPath("$.productDataList[0].discount.value").value(discount.getValue()))
                .andExpect(jsonPath("$.productDataList[0].discount.minNumberOfProducts").value(discount.getMinNumberOfProducts()));
    }


    public void testGetAllOrdersByClientId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/order/getAllOrdersByClientId/{clientId}", CLIENT_ID)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(USER, USER_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    private ResultActions createOrderForCustomer(String clientId) throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product/addNewProduct")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(ADMIN_USER, ADMIN_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product1)));

        mockMvc.perform(post("/api/v1/cart/{clientId}/addProductById/{productId}", CLIENT_ID, product1.getId())
                .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(USER, USER_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON));

        return mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/order/purchaseCart/{clientId}", CLIENT_ID)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(USER, USER_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON));
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