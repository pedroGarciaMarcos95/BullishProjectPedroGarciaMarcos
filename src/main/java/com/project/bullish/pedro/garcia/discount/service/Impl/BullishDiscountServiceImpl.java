package com.project.bullish.pedro.garcia.discount.service.Impl;

import com.project.bullish.pedro.garcia.cart.data.CartData;
import com.project.bullish.pedro.garcia.discount.data.DiscountData;
import com.project.bullish.pedro.garcia.discount.service.BullishDiscountService;
import com.project.bullish.pedro.garcia.product.data.ProductData;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class BullishDiscountServiceImpl implements BullishDiscountService {

    private final Map<Long, DiscountData> discounts = new HashMap<>();

    private long discountIdCounter = 1;

    @Override
    public DiscountData createDiscount(DiscountData discountData) {
        generateDiscountId(discountData);
        discounts.put(discountIdCounter, discountData);
        discountIdCounter++;
        return discountData;
    }

    @Override
    public DiscountData getDiscountById(Long discountId) {
        return discounts.get(discountId);
    }

    @Override
    public boolean isDiscountApplicable(CartData cart, ProductData product, int productCount) {
        DiscountData discount = product.getDiscount();
        int quantityInCart = getQuantityOfSameProductInCart(cart, product);

        if (quantityInCart >= discount.getMinNumberOfProducts() && productCount >= discount.getMinNumberOfProducts()) {
            product.getDiscount().setApplied(true);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public double applyDiscount(CartData cartData, ProductData product) {
        double originalPrice = product.getPrice();
        DiscountData discount = product.getDiscount();

        //This check is useless right now because we are checking that in the previous method.
        //I´´l let it like that just in case this method is beeing called in the future.
        if (Objects.nonNull(discount)) {
            double discountValue = discount.isPercentage()
                    ? calculatePercentageDiscount(originalPrice, discount.getValue())
                    : discount.getValue();

            double discountedPrice = originalPrice - discountValue;

            return Math.max(discountedPrice, 0);
        } else {
            return originalPrice;
        }
    }

    private void generateDiscountId(DiscountData discountData) {
        /*I have created this method for the possibility of extending or modifying
        the creation of the discount ID in the future.*/
        discountData.setId(discountIdCounter);
    }

    private int getQuantityOfSameProductInCart(CartData cart, ProductData product) {
        return cart.getProductDataList().stream()
                .filter(p -> p.getId().equals(product.getId())).toList().size();
    }

    private double calculatePercentageDiscount(double originalPrice, double percentage) {
        return originalPrice * percentage / 100;
    }

}
