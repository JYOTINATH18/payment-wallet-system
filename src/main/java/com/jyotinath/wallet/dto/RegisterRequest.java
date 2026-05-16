package com.jyotinath.wallet.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private  String name;
    private String password;
    private String email;
}
