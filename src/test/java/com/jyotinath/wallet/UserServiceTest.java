package com.jyotinath.wallet;

import com.jyotinath.wallet.config.JwtUtil;
import com.jyotinath.wallet.dto.LoginRequest;
import com.jyotinath.wallet.entity.User;
import com.jyotinath.wallet.repository.TransactionRepository;
import com.jyotinath.wallet.repository.UserRepository;
import com.jyotinath.wallet.repository.WalletRepository;
import com.jyotinath.wallet.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private  UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    @Test
    void loginSuccess(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("Jyoti");
        loginRequest.setPassword("1234");

        User user = new User();
        user.setUsername("Jyoti");
        user.setPassword("1234");

        when(userRepository.findByUsername("Jyoti"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("password","1234")).thenReturn(true);

        when(jwtUtil.generateToken("Jyoti")).thenReturn("jwt-token");

        String token = userService.login(loginRequest);

        assertEquals("jwt-token",token);

        verify(userRepository).findByUsername("Jyoti");
        verify(passwordEncoder).matches("password","1234");
        verify(jwtUtil).generateToken("Jyoti");

    }
}
