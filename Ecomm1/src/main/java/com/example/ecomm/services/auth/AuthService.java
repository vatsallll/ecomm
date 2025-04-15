package com.example.ecomm.services.auth;

import com.example.ecomm.dto.SignupRequest;
import com.example.ecomm.dto.UserDto;

public interface AuthService {

    UserDto createUser(SignupRequest signupRequest);

    Boolean hasUserWithEmail(String email);
}
