package com.project.bullish.pedro.garcia.product.controller;

import com.project.bullish.pedro.garcia.api.exception.BullishProductException;
import com.project.bullish.pedro.garcia.api.exception.BullishProductNotFoundException;
import com.project.bullish.pedro.garcia.product.data.ProductData;
import com.project.bullish.pedro.garcia.product.facade.BullishProductFacade;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.project.bullish.pedro.garcia.constants.BullishCoreConstants.ERROR_PRODUCT_NOT_FOUND_CODE;

@RestController
@RequestMapping("/api/v1/product")
public class BullishProductController {

    @Resource
    private BullishProductFacade bullishProductFacade;

    @PostMapping("/addNewProduct")
    public ResponseEntity<ProductData> createProduct(@RequestBody ProductData product) {
        ProductData addedProduct = bullishProductFacade.createProduct(product);
        return new ResponseEntity<>(addedProduct, HttpStatus.CREATED);
    }

    @DeleteMapping("/remove/{productId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<Void> deleteProduct(@PathVariable final Long productId) throws BullishProductException {

        ProductData productData = bullishProductFacade.getProductById(productId);

        if (productData == null || productId == null) {
            //I added a key here as it should be a web application and the FE part needs to be headless,
            //it´s always useful to have a key of the error in case they want to show some message.
            throw new BullishProductNotFoundException(ERROR_PRODUCT_NOT_FOUND_CODE, productId);
        }

        bullishProductFacade.deleteProduct(productId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get/allProducts")
    public ResponseEntity<List<ProductData>> getAllProducts() {
        List<ProductData> productList = bullishProductFacade.getAllProducts();
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }
}
