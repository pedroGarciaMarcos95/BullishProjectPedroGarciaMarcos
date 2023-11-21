package com.project.bullish.pedro.garcia.product.service.Impl;

import com.project.bullish.pedro.garcia.api.exception.BullishProductException;
import com.project.bullish.pedro.garcia.cart.service.BullishCartService;
import com.project.bullish.pedro.garcia.discount.data.DiscountData;
import com.project.bullish.pedro.garcia.product.data.ProductData;
import com.project.bullish.pedro.garcia.product.service.BullishProductService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BullishProductServiceImpl implements BullishProductService {

    private Map<Long, ProductData> products = new HashMap<>();
    private long productIdCounter = 1;

    @Resource
    private BullishCartService bullishCartService;

    @Override
    public ProductData createProduct(ProductData product) {
        generateProductId(product);
        products.put(productIdCounter, product);
        productIdCounter++;
        return product;
    }

    @Override
    public void deleteProduct(@Nonnull Long productId) throws BullishProductException {
        try {
            products.remove(productId);
            removeProductFromCartIfExist(productId);
        } catch (RuntimeException e) {
            throw new BullishProductException(String.format("There has been a problem  trying to remove the product with code {}", productId));
        }
    }

    @Override
    public List<ProductData> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    @Override
    public ProductData getProductById(Long productId) {
        return products.get(productId);
    }

    @Override
    public ProductData setDiscountToProduct(DiscountData discountData, Long productId) {
        ProductData productData = getProductById(productId);
        if (Objects.nonNull(productData)) {
            productData.setDiscount(discountData);
        }
        return productData;
    }

    private void generateProductId(ProductData productData) {
        /*I have created this method for the possibility of extending or modifying
        the creation of the product ID in the future.*/
        productData.setId(productIdCounter);
    }

    private void removeProductFromCartIfExist(@Nonnull Long productId) {
        bullishCartService.removeProductFromCartIfNeeded(getProductById(productId));
    }

}
