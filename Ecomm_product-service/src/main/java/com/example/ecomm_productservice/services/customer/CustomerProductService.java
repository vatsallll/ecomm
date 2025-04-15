package com.example.ecomm_productservice.services.customer;

import com.example.ecomm_productservice.dto.ProductDetailDto;
import com.example.ecomm_productservice.dto.ProductDto;

import java.util.List;

public interface CustomerProductService {

    List<ProductDto> getAllProducts();

//    List<ProductDto> getAllProductsByName(String name);

    List<ProductDto> searchProductByTitle(String title);
    ProductDetailDto getProductDetailById(Long productId);

}