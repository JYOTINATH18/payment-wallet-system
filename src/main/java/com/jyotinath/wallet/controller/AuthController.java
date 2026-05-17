package com.jyotinath.wallet.controller;

import com.jyotinath.wallet.dto.ApiResponse;
import com.jyotinath.wallet.dto.RegisterRequest;
import com.jyotinath.wallet.entity.User;
import com.jyotinath.wallet.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(
            @RequestBody RegisterRequest request){
        try{
            User user = userService.register(request);
            return ResponseEntity.ok(
                    new ApiResponse(true,
                            "User Register Successfully!",
                            user.getUsername()));
        } catch (RuntimeException r){
                return ResponseEntity.badRequest().body(new ApiResponse(false, r.getMessage(), null));
        }
    }
}
