package com.example.ecomm_productservice.services.admin.faq;


import com.example.ecomm_productservice.dto.FAQDto;
import com.example.ecomm_productservice.entity.FAQ;
import com.example.ecomm_productservice.entity.Product;
import com.example.ecomm_productservice.repository.FAQRepository;
import com.example.ecomm_productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FAQServiceImpl implements FAQService {

    private final FAQRepository faqRepository;

    private final ProductRepository productRepository;

    public FAQDto postFAQ (Long productId, FAQDto faqDto){
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            FAQ faq = new FAQ();
            faq.setQuestion (faqDto.getQuestion());
            faq.setAnswer (faqDto.getAnswer());
            faq.setProduct (optionalProduct.get());

            return faqRepository.save(faq).getFAQDto();
        }
        return null;
    }
}