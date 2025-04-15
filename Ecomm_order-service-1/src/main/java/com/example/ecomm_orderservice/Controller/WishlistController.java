package com.example.ecomm_orderservice.Controller;

import com.example.ecomm_orderservice.dto.WishlistDto;
import com.example.ecomm_orderservice.entity.Wishlist;
import com.example.ecomm_orderservice.repository.WishlistRepository;
import com.example.ecomm_orderservice.services.wishlist.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    private final WishlistRepository wishlistRepository;

    @PostMapping("/wishlist")
    public ResponseEntity<?> addProductToWishlist (@RequestBody WishlistDto wishlistDto) {

        Wishlist wishlist = wishlistRepository
                .findByProductIdAndUserId(wishlistDto.getProductId(), wishlistDto.getUserId())
                .orElse(null);

        if(wishlist != null) {
              return new ResponseEntity<>(HttpStatus.CONFLICT);
        }


        WishlistDto postedWishlistDto = wishlistService.addProductToWishlist (wishlistDto);


        if (postedWishlistDto == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");

        return ResponseEntity. status (HttpStatus.CREATED). body (postedWishlistDto);
    }

    @GetMapping("/wishlist/{userId}")
    public ResponseEntity<List<WishlistDto>> getWishlistByUserId(@PathVariable Long userId){


        return ResponseEntity.ok(wishlistService.getWishlistByUserId(userId));
    }


    @GetMapping("removewishlist/{id}")
    public ResponseEntity<?> removeWishlist(@PathVariable Long id) {
        Wishlist wishlist = wishlistRepository.findById(id).orElse(null);
        Map<String, Object> response = new HashMap<>();

        if (wishlist != null) {
            wishlistRepository.delete(wishlist); // Remove the entity from the repository
            response.put("status", "success");
            response.put("message", "Wishlist item removed successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "Wishlist item not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


}