package com.project.bullish.pedro.garcia.discount.facade.Impl;

import com.project.bullish.pedro.garcia.api.exception.BullishDiscountNotFoundException;
import com.project.bullish.pedro.garcia.discount.data.DiscountData;
import com.project.bullish.pedro.garcia.discount.facade.BullishDiscountFacade;
import com.project.bullish.pedro.garcia.discount.service.BullishDiscountService;
import com.project.bullish.pedro.garcia.product.data.ProductData;
import com.project.bullish.pedro.garcia.product.service.BullishProductService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static com.project.bullish.pedro.garcia.constants.BullishCoreConstants.ERROR_DISCOUNT_NOT_FOUND_CODE;

@Component
public class BullishDiscountFacadeImpl implements BullishDiscountFacade {

    @Resource
    private BullishDiscountService bullishDiscountService;
    @Resource
    private BullishProductService bullishProductService;

    @Override
    public DiscountData createDiscount(DiscountData discountData) {
        return bullishDiscountService.createDiscount(discountData);
    }

    @Override
    public Optional<ProductData> assignDiscountToProduct(Long productId, Long discountId) {
        DiscountData discount = bullishDiscountService.getDiscountById(discountId);
        if (Objects.isNull(discount)) {
            throw new BullishDiscountNotFoundException(ERROR_DISCOUNT_NOT_FOUND_CODE, discountId);
        }

        return Optional.ofNullable(bullishProductService.setDiscountToProduct(discount, productId));
    }

    @Override
    public Optional<ProductData> createAndAssignDiscountToProduct(DiscountData discountData, Long productId) {
        DiscountData createdDiscount = bullishDiscountService.createDiscount(discountData);
        return assignDiscountToProduct(productId, createdDiscount.getId());
    }

}
