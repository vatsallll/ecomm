package com.example.ecomm.controller;

import com.example.ecomm.dto.AuthenticationRequest;
import com.example.ecomm.dto.SignupRequest;
import com.example.ecomm.dto.UserDto;
import com.example.ecomm.entity.User;
import com.example.ecomm.repository.UserRepository;
import com.example.ecomm.services.auth.AuthService;
import com.example.ecomm.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    private final AuthService authService;

    @PostMapping("/authenticate")
    public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
                                          HttpServletResponse response) throws IOException, JSONException {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch(BadCredentialsException e){
            throw new BadCredentialsException("Incorrect username or password");
        }

        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        Optional<User> optionalUser = this.userRepository.findFirstByEmail(userDetails.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        if(optionalUser.isPresent()){
            response.getWriter().write(new JSONObject()
                    .put("userId", optionalUser.get().getId())
                    .put("role", optionalUser.get().getRole())
                    .toString()
            );

            response.addHeader("Access-Control-Expose-Headers","Authorization");
            response.addHeader("Access-Control-Allow-Headers","Authorization, X-PINGOTHER, Origin" +
                    "X-Requested-With, Content-Type, Accept, X-Custom-Header");

            response.addHeader("Authorization", "Bearer " + jwt);

            System.out.println("Authorization Header Sent: " + response.getHeader("Authorization"));
        }
    }
//@PostMapping("/authenticate")
//public void createAuthenticationToken(
//        @RequestBody AuthenticationRequest authenticationRequest,
//        HttpServletResponse response
//) throws IOException, JSONException {
//    try {
//        // 1. Authenticate
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        authenticationRequest.getUsername(),
//                        authenticationRequest.getPassword()
//                )
//        );
//
//        // 2. Generate JWT (only reaches here if auth succeeds)
//        UserDetails userDetails = userDetailsService.loadUserByUsername(
//                authenticationRequest.getUsername()
//        );
//        String jwt = jwtUtil.generateToken(userDetails.getUsername());
//
//        // 3. Prepare response
//        Optional<User> user = userRepository.findFirstByEmail(userDetails.getUsername());
//        if (user.isPresent()) {
//            response.setContentType("application/json");
//            response.getWriter().write(
//                    new JSONObject()
//                            .put("userId", user.get().getId())
//                            .put("role", user.get().getRole())
//                            .toString()
//            );
//
//            response.addHeader("Access-Control-Expose-Headers","Authorization");
//
//            response.addHeader("Access-Control-Allow-Headers","Authorization, X-PINGOTHER, Origin" +
//                    "X-Requested-With, Content-Type, Accept, X-Custom-Header");
//
//            response.addHeader("Authorization", "Bearer " + jwt);
//        }
//
//    } catch (BadCredentialsException e) {
//        // 4. Proper error response
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
//        response.getWriter().write(
//                new JSONObject()
//                        .put("error", "Invalid credentials")
//                        .toString()
//        );
//    }
//}

    @PostMapping(value="/sign-up")
    public ResponseEntity<?> signupUser (@RequestBody SignupRequest signupRequest) {
        if((authService.hasUserWithEmail(signupRequest.getEmail()))){
            return new ResponseEntity<>("User with this email already exists", HttpStatus.NOT_ACCEPTABLE);
        }
        UserDto userDto = authService.createUser (signupRequest);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
