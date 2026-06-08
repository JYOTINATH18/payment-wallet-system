package com.jyotinath.wallet.controller;

import com.jyotinath.wallet.dto.ApiResponse;
import com.jyotinath.wallet.dto.DepositeRequest;
import com.jyotinath.wallet.dto.TransferRequest;
import com.jyotinath.wallet.entity.Transaction;
import com.jyotinath.wallet.service.UserService;
import com.jyotinath.wallet.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/wallet")
public class WalletController {
    private final WalletService walletService;

    private final UserService userService;

    public WalletController(WalletService walletService, UserService userServices){
        this.walletService = walletService;
        this.userService = userServices;
    }

    private String getCurrentUsername(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @GetMapping("/balance")
    public ResponseEntity<ApiResponse> getBalance(){
        try {
            String username = getCurrentUsername();
            Long userId = userService.getUserByUsername(username).getId();

            BigDecimal balance = walletService.getBalance(userId);

            return ResponseEntity.ok(new ApiResponse(true, "Balance fetched", balance));
        }catch ( RuntimeException e){
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping("/transfer")
        public ResponseEntity<ApiResponse> transfer(@RequestBody TransferRequest request){
            try {
                String username = getCurrentUsername();
                Long userId = userService.getUserByUsername(username).getId();

                String result = walletService.transfer(userId, request);

                return ResponseEntity.ok(new ApiResponse(true, result, null));
            }catch (RuntimeException e){
                return ResponseEntity.badRequest().body(new ApiResponse(false,e.getMessage(), null));
            }
        }

        @GetMapping("/history")
    public ResponseEntity<ApiResponse> getHistory(){
        try{
            String username = getCurrentUsername();
            long user_id = userService.getUserByUsername(username).getId();

            List<Transaction> history = walletService.getHistory(user_id);

            return ResponseEntity.ok(new ApiResponse(true, "History fetched!", history));
        }catch (RuntimeException e){
            return  ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
        }

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse> deposit(@RequestBody DepositeRequest amount) {
        try {
            String username = getCurrentUsername();
            long user_id = userService.getUserByUsername(username).getId();
            walletService.deposit(username, user_id, amount);
            return ResponseEntity.ok(new ApiResponse(true, "Amount deposited successfully", null));
        } catch (RuntimeException e){
        return ResponseEntity.badRequest().body(new ApiResponse(false,e.getMessage(), null));
    }
    }
}
