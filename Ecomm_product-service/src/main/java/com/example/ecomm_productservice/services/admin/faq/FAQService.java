package com.example.ecomm_productservice.services.admin.faq;

import com.example.ecomm_productservice.dto.FAQDto;

public interface FAQService {

    FAQDto postFAQ (Long productId, FAQDto faqDto);
}