package com.example.ecomm_orderservice.repository;

import com.example.ecomm_orderservice.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findAllByUserId(Long userId);
    Optional<Wishlist> findByProductIdAndUserId(Long productId, Long userId);

}