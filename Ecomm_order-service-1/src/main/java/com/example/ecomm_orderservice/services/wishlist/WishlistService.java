package com.example.ecomm_orderservice.services.wishlist;

import com.example.ecomm_orderservice.dto.WishlistDto;

import java.util.List;

public interface WishlistService {

    WishlistDto addProductToWishlist (WishlistDto wishlistDto);

    List<WishlistDto> getWishlistByUserId(Long userId);
}