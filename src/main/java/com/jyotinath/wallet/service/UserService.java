package com.jyotinath.wallet.service;

import com.jyotinath.wallet.dto.RegisterRequest;
import com.jyotinath.wallet.entity.User;
import com.jyotinath.wallet.entity.Wallet;
import com.jyotinath.wallet.repository.UserRepository;
import com.jyotinath.wallet.repository.WalletRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor injection
    public UserService(UserRepository userRepository,
                       WalletRepository walletRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegisterRequest request) {
        // Check if username exists
        if(userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("Username already exists!");

        // Check if email exists
        if(userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("Email already exists!");

        // Create user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());

        // Save user
        User savedUser = userRepository.save(user);

        // Create wallet for user automatically
        Wallet wallet = new Wallet();
        wallet.setUser(savedUser);
        wallet.setBalance(BigDecimal.ZERO);
        walletRepository.save(wallet);

        return savedUser;
    }
}