package com.example.ecomm_orderservice.Controller;

import com.example.ecomm_orderservice.dto.AddProductInCartDto;
import com.example.ecomm_orderservice.dto.OrderDto;
import com.example.ecomm_orderservice.dto.PlaceOrderDto;
import com.example.ecomm_orderservice.dto.UserDto;
import com.example.ecomm_orderservice.entity.CartItems;
import com.example.ecomm_orderservice.entity.Product;
import com.example.ecomm_orderservice.entity.User;
import com.example.ecomm_orderservice.repository.CartItemsRepository;
import com.example.ecomm_orderservice.repository.ProductRepository;
import com.example.ecomm_orderservice.repository.UserRepository;
import com.example.ecomm_orderservice.services.cart.CartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final CartItemsRepository cartItemsRepository;
    @PostMapping("/cart")
    public ResponseEntity<?> addProductToCart(@RequestBody AddProductInCartDto addProductInCartDto) {
         logger.info("Add product to Cart");
        return cartService.addProductToCart(addProductInCartDto);
    }

    @GetMapping("/cart/{userId}")
    public ResponseEntity<?> getCartByUserId(@PathVariable Long userId) {
  logger.info("Request received at /getcart endpoint");
        OrderDto orderDto = cartService.getCartByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(orderDto);
    }
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @PostMapping("/addition")
    public ResponseEntity<OrderDto> increaseProductQuantity(@RequestBody AddProductInCartDto addProductInCartDto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.increaseProductQuantity(addProductInCartDto));
    }

    @PostMapping("/deduction")
    public ResponseEntity<OrderDto> decreaseProductQuantity(@RequestBody AddProductInCartDto addProductInCartDto) {
        //System.out.println("yo");
        logger.info("Request received at /deduction endpoint");
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.decreaseProductQuantity(addProductInCartDto));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProductFromCart(@RequestBody AddProductInCartDto addProductInCartDto) {
        logger.info("Request received at /delete endpoint");
        try {
            cartService.deleteProductFromCart(addProductInCartDto);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Product successfully removed from the cart.");
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the product.");
        }
    }
    @PostMapping("/placeOrder")
    public ResponseEntity<OrderDto> placeOrder(@RequestBody PlaceOrderDto placeOrderDto) {
        logger.info("Request received at /placeorder endpoint");
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.placeOrder(placeOrderDto));
    }

    @GetMapping("/myOrders/{userId}")
    public ResponseEntity<List<OrderDto>> getMyPlacedOrders (@PathVariable Long userId) {
        return ResponseEntity.ok (cartService.getMyPlacedOrders (userId));
    }
    @GetMapping( "GetUser/{userid}")
    public ResponseEntity<?> getUser(@PathVariable Long userid) {
        User user = userRepository.findById(userid).orElse(null);
        UserDto dto = new UserDto();
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return new ResponseEntity<>(dto, HttpStatus.OK);

    }
   @PostMapping("changeusername/{userId}")
    public ResponseEntity<?> changeUsername(
            @PathVariable Long userId,
            @RequestBody Map<String, String> requestBody) {
        try {
            String newUsername = requestBody.get("username");
            User user = userRepository.findById(userId).orElse(null);

            if(user != null) {
                user.setName(newUsername);
                userRepository.save(user);
            }
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Username updated successfully");
            response.put("userId", userId);
            response.put("newUsername", newUsername);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Failed to update username");
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }



       }
    @GetMapping("quantity/{productId}")
    public ResponseEntity<?> quantity(@PathVariable Long productId) {

        Product product = productRepository.findById(productId).orElse(null);
        if(product != null) {
            Long quantity = product.getQuantity();
            return ResponseEntity.status(HttpStatus.OK).body(quantity);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }

    @GetMapping("/postquantity/{productId}/{quantity}/{userId}/{orderId}")
    public ResponseEntity<?> postQuantity(@PathVariable Long productId, @PathVariable Long quantity ,@PathVariable Long userId, @PathVariable Long orderId ) {
        CartItems cart = cartItemsRepository.findByProductIdAndOrderIdAndUserId(productId,orderId,userId).orElse(null);
        //logger.info("Request received at /postquantity endpoint");
        if(cart != null) {
            cart.setQuantity(quantity);
            cartItemsRepository.save(cart);
            return ResponseEntity.status(HttpStatus.OK).body(cart);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }

    @GetMapping("/getquantity/{productId}/{quantity}/{userId}/{orderId}")
        public ResponseEntity<?> getQuantity(@PathVariable Long productId, @PathVariable Long quantity ,@PathVariable Long userId, @PathVariable Long orderId){
        CartItems cart = cartItemsRepository.findByProductIdAndOrderIdAndUserId(productId,orderId,userId).orElse(null);
        if(cart != null) {


            return ResponseEntity.status(HttpStatus.OK).body(cart.getQuantity());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }

    @GetMapping("updateproductquantity/{id}")

    public ResponseEntity<?> updateProductQuantity(@PathVariable Long id) {
        logger.info("Request received at /updateproductquantity endpoint");
        // Fetch the cart items based on the order ID
        List<CartItems> cart = cartItemsRepository.findByOrderId(id);

        if (cart == null || cart.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No cart items found for the given order ID.");
        }

        // Iterate through each cart item
        for (CartItems cartItem : cart) {
            Long productId = cartItem.getProduct().getId();
            Long cartQuantity = cartItem.getQuantity();

            // Fetch the product from the Product table
            Optional<Product> productOpt = productRepository.findById(productId);

            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                long updatedQuantity = product.getQuantity() - cartQuantity;

                if (updatedQuantity < 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            "Insufficient stock for product ID: " + productId
                    );
                }

                // Update the product's quantity
                product.setQuantity(updatedQuantity);
                productRepository.save(product);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        "Product not found for ID: " + productId
                );
            }
        }

        return ResponseEntity.ok("Product quantities updated successfully.");
    }

}



    




