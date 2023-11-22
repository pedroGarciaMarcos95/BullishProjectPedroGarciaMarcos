package com.project.bullish.pedro.garcia.cart.service.impl;

import com.project.bullish.pedro.garcia.cart.data.CartData;
import com.project.bullish.pedro.garcia.cart.service.BullishCartService;
import com.project.bullish.pedro.garcia.discount.service.BullishDiscountService;
import com.project.bullish.pedro.garcia.product.data.ProductData;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BullishCartServiceImpl implements BullishCartService {

    private Map<String, CartData> carts = new HashMap<>();

    @Resource
    private BullishDiscountService bullishDiscountService;

    @Override
    public CartData getOrCreateCartById(String clientId) {
        if (carts.containsKey(clientId)) {
            return carts.get(clientId);
        } else {
            CartData cart = new CartData();
            cart.setId(clientId);
            carts.put(clientId, cart);
            return cart;
        }
    }

    @Override
    public CartData getCartById(String clientId) {
        return carts.getOrDefault(clientId, null);
    }

    @Override
    public void addProductToCart(@Nonnull CartData cart, ProductData product) {
        if (cart.getProductDataList() == null) {
            cart.setProductDataList(new ArrayList<>());
        }
        cart.getProductDataList().add(product);
        calculateTotals(cart);
    }

    @Override
    public CartData removeProductFromCartById(@Nonnull CartData cart, Long productId) {
        if (cart.getProductDataList() != null) {
            cart.getProductDataList().removeIf(product -> product.getId().equals(productId));
            calculateTotals(cart);
        }
        return cart;
    }

    @Override
    public void removeProductFromCartIfNeeded(ProductData productData) {
        //This method removes all products on all carts. I could create method just to remove a product for 1 cart.
        carts.values()
                .forEach(cart -> {
                    cart.getProductDataList().removeIf(product -> product.equals(productData));
                    calculateTotals(cart);
                });
    }

    @Override
    public void clearCart(String clientId) {
        carts.remove(clientId);
    }

    private void calculateTotals(CartData cart) {
        //First approach of a calculateTotals method with the calculations of the promotions.
        //This map was created with AtomicInteger because I had to get the number of same product con each cart.
        Map<String, AtomicInteger> productCountMap = new HashMap<>();

        double total = cart.getProductDataList().stream()
                .mapToDouble(product -> {
                    double productPrice = product.getPrice();
                    String productId = product.getId().toString();

                    //I used computeIfAbsent method that memorize the Product id of each product.
                    //With this I was able to create a custom map for each product by ID.
                    AtomicInteger productCount = productCountMap.computeIfAbsent(productId, k -> new AtomicInteger(1));

                    if (Objects.nonNull(product.getDiscount())) {
                        boolean isDiscountApplicable = bullishDiscountService.isDiscountApplicable(cart, product, productCount.get());
                        productCount.incrementAndGet();

                        return isDiscountApplicable
                                ? bullishDiscountService.applyDiscount(cart, product)
                                : productPrice;
                    } else {
                        productCount.incrementAndGet();
                        return productPrice;
                    }
                }).sum();

        cart.setTotalAmount(total);
    }

}
