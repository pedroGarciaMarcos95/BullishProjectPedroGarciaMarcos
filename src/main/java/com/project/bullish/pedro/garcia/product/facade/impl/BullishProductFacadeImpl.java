package com.project.bullish.pedro.garcia.product.facade.impl;

import com.project.bullish.pedro.garcia.api.exception.BullishProductException;
import com.project.bullish.pedro.garcia.product.data.ProductData;
import com.project.bullish.pedro.garcia.product.facade.BullishProductFacade;
import com.project.bullish.pedro.garcia.product.service.BullishProductService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BullishProductFacadeImpl implements BullishProductFacade {

    @Resource
    private BullishProductService bullishProductService;

    @Override
    public ProductData createProduct(ProductData product) {
        return bullishProductService.createProduct(product);
    }

    @Override
    public void deleteProduct(@Nonnull Long productId) throws BullishProductException {
        bullishProductService.deleteProduct(productId);
    }

    @Override
    public List<ProductData> getAllProducts() {
        return bullishProductService.getAllProducts();
    }

    @Override
    public ProductData getProductById(Long productId) {
        return bullishProductService.getProductById(productId);
    }

}
