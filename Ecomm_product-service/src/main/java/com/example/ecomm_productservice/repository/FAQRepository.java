package com.example.ecomm_productservice.repository;

import com.example.ecomm_productservice.entity.FAQ;
import com.example.ecomm_productservice.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FAQRepository extends JpaRepository<FAQ, Long> {

    List<FAQ> findAllByProductId(Long productsId);

}