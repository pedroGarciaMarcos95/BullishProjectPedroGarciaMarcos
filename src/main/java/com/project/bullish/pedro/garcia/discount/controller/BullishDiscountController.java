package com.project.bullish.pedro.garcia.discount.controller;

import com.project.bullish.pedro.garcia.api.exception.BullishProductNotFoundException;
import com.project.bullish.pedro.garcia.discount.data.DiscountData;
import com.project.bullish.pedro.garcia.discount.facade.BullishDiscountFacade;
import com.project.bullish.pedro.garcia.product.data.ProductData;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.project.bullish.pedro.garcia.constants.BullishCoreConstants.ERROR_PRODUCT_NOT_FOUND_CODE;

@RestController
@RequestMapping("/api/v1/discount")
public class BullishDiscountController {

    @Resource
    private BullishDiscountFacade bullishDiscountFacade;

    @PostMapping("/create")
    public ResponseEntity<DiscountData> createDiscount(@RequestBody DiscountData discountData) {
        DiscountData createdDiscount = bullishDiscountFacade.createDiscount(discountData);
        return new ResponseEntity<>(createdDiscount, HttpStatus.CREATED);
    }

    @PostMapping("/assign/{productId}/{discountId}")
    public ResponseEntity<ProductData> assignDiscountToProduct(
            @PathVariable Long productId,
            @PathVariable Long discountId) {
        return new ResponseEntity<>(bullishDiscountFacade.assignDiscountToProduct(productId, discountId)
                .orElseThrow(() -> new BullishProductNotFoundException(ERROR_PRODUCT_NOT_FOUND_CODE, productId)),
                HttpStatus.OK);
    }

    @PostMapping("/create-and-assign/{productId}")
    public ResponseEntity<ProductData> createAndAssignDiscountToProduct(
            @RequestBody DiscountData discountData,
            @PathVariable Long productId) {
        return new ResponseEntity<>(bullishDiscountFacade.createAndAssignDiscountToProduct(discountData, productId)
                .orElseThrow(() -> new BullishProductNotFoundException(ERROR_PRODUCT_NOT_FOUND_CODE, productId)),
                HttpStatus.OK);
    }

}
