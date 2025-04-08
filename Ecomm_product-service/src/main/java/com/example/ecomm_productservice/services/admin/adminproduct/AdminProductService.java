package com.example.ecomm_productservice.services.admin.adminproduct;

import com.example.ecomm_productservice.dto.ProductDto;

import java.io.IOException;
import java.util.List;

public interface AdminProductService {

    ProductDto addProduct (ProductDto productDto) throws IOException;

    List<ProductDto> getAllProducts();
}
