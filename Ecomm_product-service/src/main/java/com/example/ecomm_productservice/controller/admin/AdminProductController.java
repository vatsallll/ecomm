package com.example.ecomm_productservice.controller.admin;

import com.example.ecomm_productservice.dto.FAQDto;
import com.example.ecomm_productservice.dto.ProductDto;
import com.example.ecomm_productservice.entity.Product;
import com.example.ecomm_productservice.services.admin.adminproduct.AdminProductService;
import com.example.ecomm_productservice.services.admin.faq.FAQService;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;

    private final FAQService faqService;
    private static final Logger logger = LoggerFactory.getLogger(AdminProductController.class);
    @PostMapping("/product")
    public ResponseEntity<ProductDto> addProduct(@ModelAttribute ProductDto productDto) throws IOException {

        ProductDto productDto1 = adminProductService.addProduct(productDto);
        //logger.info(String.valueOf(productDto.getQuantity()));
        return ResponseEntity.status(HttpStatus.CREATED).body(productDto1);
    }

    @GetMapping("/products")
    public ResponseEntity<Page<ProductDto>> getProducts(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = adminProductService.getPaginatedProducts(pageable);
        Page<ProductDto> dtoPage = productPage.map(product -> {
            ProductDto dto = new ProductDto();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setPrice(product.getPrice());
            dto.setCategoryName(product.getCategory().getName()); // Assuming category is an object
            dto.setDescription(product.getDescription());
            dto.setByteImg(product.getImg());
            dto.setQuantity(product.getQuantity());

            // Set all other fields you need
            return dto;
        });
        return ResponseEntity.ok(dtoPage);
    }
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId){
      boolean deleted = adminProductService.deleteProduct(productId);
      if(deleted){
          return ResponseEntity.noContent().build();
      }
      return ResponseEntity.notFound().build();
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<Page<ProductDto>> getProductsByName(@PathVariable String name , @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = adminProductService.getProductsByName(name,pageable);
        Page<ProductDto> dtoPage = productPage.map(product -> {
            ProductDto dto = new ProductDto();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setPrice(product.getPrice());
            dto.setCategoryName(product.getCategory().getName()); // Assuming category is an object
            dto.setDescription(product.getDescription());
            dto.setByteImg(product.getImg());
            // Set all other fields you need
            return dto;
        });
        return ResponseEntity.ok(dtoPage);
    }

    @PutMapping("/product/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long productId, @ModelAttribute ProductDto productDto) throws IOException {
        logger.info("in /update");
        ProductDto updatedProduct = adminProductService.updateProduct(productId, productDto);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {
        ProductDto productDto = adminProductService.getProductById(productId);
        if (productDto != null) {
            return ResponseEntity.ok(productDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/faq/{productId}")
    public ResponseEntity<FAQDto> postFAQ (@PathVariable Long productId, @RequestBody FAQDto faqDto){
        return ResponseEntity.status(HttpStatus.CREATED). body (faqService.postFAQ (productId, faqDto));
    }



}
