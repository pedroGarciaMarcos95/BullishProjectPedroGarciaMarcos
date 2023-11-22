package com.project.bullish.pedro.garcia.cart.controller;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(loader = SpringBootContextLoader.class)
class BullishCartControllerTest {

    @Resource
    private MockMvc mockMvc;

    @Resource
    private ObjectMapper objectMapper;

    private static String ADMIN_USER = "admin";
    private static String ADMIN_PASSWORD = "nimda";

    private static String USER = "user";
    private static String USER_PASSWORD = "password";

    private static String CLIENT_ID = "CartId";

    private ProductData product1;
    private ProductData product2;
    private ProductData product3;
    private ProductData product4;
    private ProductData testProduct;
    private DiscountData discount;
    private ResultActions resultActions;

    @BeforeEach
    void setUp() {
        createProduct();
        createDiscount();
        assingDiscountToProduct(discount, product1);
        assingDiscountToProduct(discount, product3);
        assingDiscountToProduct(discount, product4);
    }

    @Test
    void testCartMethods() throws Exception {
        removeAProductFromCart();
        addProductByIdToCart();
        getCartByClientId();
    }

    void addProductByIdToCart() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product/addNewProduct")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(ADMIN_USER, ADMIN_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product3)));

        //I had to do this strange logic as I´m saving the objects on a Java data structure and the ID get dynamically incremented.
        try {
            resultActions = mockMvc.perform(post("/api/v1/cart/{clientId}/addProductById/{productId}", CLIENT_ID, product3.getId())
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(USER, USER_PASSWORD))
                    .contentType(MediaType.APPLICATION_JSON));
            testProduct = product3;

        } catch (Exception exception) {
            resultActions = mockMvc.perform(post("/api/v1/cart/{clientId}/addProductById/{productId}", CLIENT_ID, product1.getId())
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(USER, USER_PASSWORD))
                    .contentType(MediaType.APPLICATION_JSON));
            testProduct = product3;
            testProduct.setId(1L);
        }

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.totalAmount").value(testProduct.getPrice()))
                .andExpect(jsonPath("$.productDataList").isArray())
                .andExpect(jsonPath("$.productDataList[0].id").value(testProduct.getId()))
                .andExpect(jsonPath("$.productDataList[0].name").value(testProduct.getName()))
                .andExpect(jsonPath("$.productDataList[0].price").value(testProduct.getPrice()))
                .andExpect(jsonPath("$.productDataList[0].discount.id").value(discount.getId()))
                .andExpect(jsonPath("$.productDataList[0].discount.percentage").value(discount.isPercentage()))
                .andExpect(jsonPath("$.productDataList[0].discount.value").value(discount.getValue()))
                .andExpect(jsonPath("$.productDataList[0].discount.minNumberOfProducts").value(discount.getMinNumberOfProducts()));

        mockMvc.perform(delete("/api/v1/cart/{clientId}/removeProduct/{productId}", CLIENT_ID, testProduct.getId())
                .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(USER, USER_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON));

    }


    void getCartByClientId() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product/addNewProduct")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(ADMIN_USER, ADMIN_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product4)));

        try {
            mockMvc.perform(post("/api/v1/cart/{clientId}/addProductById/{productId}", CLIENT_ID, product4.getId())
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(USER, USER_PASSWORD))
                    .contentType(MediaType.APPLICATION_JSON));
            testProduct = product4;

        } catch (Exception exception) {
            mockMvc.perform(post("/api/v1/cart/{clientId}/addProductById/{productId}", CLIENT_ID, product1.getId())
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(USER, USER_PASSWORD))
                    .contentType(MediaType.APPLICATION_JSON));
            testProduct = product4;
            testProduct.setId(1L);
        }

        mockMvc.perform(post("/api/v1/cart/getCartByClientId/{clientId}", CLIENT_ID)
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(USER, USER_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.totalAmount").value(testProduct.getPrice()))
                .andExpect(jsonPath("$.productDataList").isArray())
                .andExpect(jsonPath("$.productDataList[0].id").value(testProduct.getId().intValue()))
                .andExpect(jsonPath("$.productDataList[0].name").value(testProduct.getName()))
                .andExpect(jsonPath("$.productDataList[0].price").value(testProduct.getPrice()))
                .andExpect(jsonPath("$.productDataList[0].discount.id").value(discount.getId()))
                .andExpect(jsonPath("$.productDataList[0].discount.percentage").value(discount.isPercentage()))
                .andExpect(jsonPath("$.productDataList[0].discount.value").value(discount.getValue()))
                .andExpect(jsonPath("$.productDataList[0].discount.minNumberOfProducts").value(discount.getMinNumberOfProducts()));

        mockMvc.perform(
                delete("/api/v1/cart/{clientId}/removeProduct/{productId}", CLIENT_ID, testProduct.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(USER, USER_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON));

    }


    void removeAProductFromCart() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product/addNewProduct")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(ADMIN_USER, ADMIN_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product1)));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product/addNewProduct")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(ADMIN_USER, ADMIN_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product2)));

        mockMvc.perform(post("/api/v1/cart/{clientId}/addProductById/{productId}", CLIENT_ID, product1.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(USER, USER_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productDataList.size()").value(1));

        mockMvc.perform(post("/api/v1/cart/{clientId}/addProductById/{productId}", CLIENT_ID, product2.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(USER, USER_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productDataList.size()").value(2));

        mockMvc.perform(delete("/api/v1/cart/{clientId}/removeProduct/{productId}", CLIENT_ID, product1.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + getBase64Credentials(USER, USER_PASSWORD))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.totalAmount").value(product2.getPrice()))
                .andExpect(jsonPath("$.productDataList").isArray())
                .andExpect(jsonPath("$.productDataList.size()").value(1))
                .andExpect(jsonPath("$.productDataList[0].id").value(product2.getId().intValue()))
                .andExpect(jsonPath("$.productDataList[0].name").value(product2.getName()))
                .andExpect(jsonPath("$.productDataList[0].price").value(product2.getPrice()))
                .andExpect(jsonPath("$.productDataList[0].discount").doesNotExist());

        mockMvc.perform(delete("/api/v1/cart/{clientId}/removeProduct/{productId}", CLIENT_ID, product2.getId())
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

        product3 = new ProductData();
        product3.setId(3L);
        product3.setPrice(1230.0);
        product3.setName("Product 3");

        product4 = new ProductData();
        product4.setId(4L);
        product4.setPrice(130.0);
        product4.setName("Product 4");
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