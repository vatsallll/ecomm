package com.example.ecomm_productservice.services.admin.adminproduct;

import com.example.ecomm_productservice.dto.ProductDto;
import com.example.ecomm_productservice.entity.Category;
import com.example.ecomm_productservice.entity.Product;
import com.example.ecomm_productservice.repository.CategoryRepository;
import com.example.ecomm_productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService{

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    public ProductDto addProduct (ProductDto productDto) throws IOException {
        Product product = new Product();
        product.setName (productDto.getName());
        product.setDescription (productDto.getDescription());
        product.setPrice (productDto.getPrice());
        product.setImg (productDto.getImg().getBytes());
        product.setQuantity(productDto.getQuantity());

        Category category = categoryRepository.findById(productDto.getCategoryId()).orElseThrow();

        product.setCategory(category);
        return productRepository.save(product).getDto();
    }

    public List<ProductDto> getAllProducts(){
        List<Product> products = productRepository.findAll();
        return products.stream().map(Product::getDto).collect(Collectors.toList());
    }
    public Page<Product> getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable);
    }
    public Page<Product> getPaginatedProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public boolean deleteProduct(Long id){
        Optional<Product> optionalProduct = productRepository.findById(id);
        if(optionalProduct.isPresent()){
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Page<Product> getProductsByName(String name,Pageable pageable){
        return productRepository.findByNameContaining(name,pageable);

    }

    public ProductDto updateProduct(Long productId, ProductDto productDto) throws IOException {

        Optional <Product> optionalProduct = productRepository.findById(productId);
        Optional<Category> optionalCategory = categoryRepository.findById(productDto.getCategoryId());
        if(optionalProduct.isPresent() && optionalCategory.isPresent()){
            Product product = optionalProduct.get();

            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setPrice(productDto.getPrice());
            product.setCategory(optionalCategory.get());
            product.setQuantity(productDto.getQuantity());
            if(productDto.getImg() != null){
                product.setImg(productDto.getImg().getBytes());
            }
            return productRepository.save(product).getDto();
        }else {
            return null;
        }
    }

    public ProductDto getProductById(Long productId) {
        Optional <Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get().getDto();
        }else{
            return null;
        }
    }
}
