package com.jyotinath.wallet.controller;

import com.jyotinath.wallet.dto.ApiResponse;
import com.jyotinath.wallet.dto.LoginRequest;
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

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request){
        try{
            String token = userService.login(request);

            return  ResponseEntity.ok(new ApiResponse(true, "Login Successful!", token));
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(),null));
        }
    }
}
