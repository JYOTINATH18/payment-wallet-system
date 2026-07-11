package com.jyotinath.wallet;

import com.jyotinath.wallet.config.JwtUtil;
import com.jyotinath.wallet.dto.LoginRequest;
import com.jyotinath.wallet.dto.RegisterRequest;
import com.jyotinath.wallet.entity.User;
import com.jyotinath.wallet.entity.Wallet;
import com.jyotinath.wallet.repository.TransactionRepository;
import com.jyotinath.wallet.repository.UserRepository;
import com.jyotinath.wallet.repository.WalletRepository;
import com.jyotinath.wallet.service.UserService;
import net.bytebuddy.implementation.bind.annotation.Argument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    void loginSuccess() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("Jyoti");
        loginRequest.setPassword("1234");

        User user = new User();
        user.setUsername("Jyoti");
        user.setPassword("1234");

        when(userRepository.findByUsername("Jyoti"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("1234", "1234"))
                .thenReturn(true);

        when(jwtUtil.generateToken("Jyoti"))
                .thenReturn("jwt-token");

        String token = userService.login(loginRequest);

        assertEquals("jwt-token", token);

        verify(userRepository).findByUsername("Jyoti");
        verify(passwordEncoder).matches("1234", "1234");
        verify(jwtUtil).generateToken("Jyoti");
    }

    @Test
    void loginUserNotFound(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("Jyoti");
        loginRequest.setPassword("1234");

        when(userRepository.findByUsername("Jyoti")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, ()-> userService.login(loginRequest));

        assertEquals("User not found!", exception.getMessage());
    }

@Test
    void loginInvalidPassword(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("Jyoti");
        loginRequest.setPassword("1234");

        User user = new User();
        user.setUsername("Jyoti");
        user.setPassword("encryptedpassword");

        when(userRepository.findByUsername("Jyoti")).thenReturn(Optional.of(user));

        when(passwordEncoder.matches("1234", "encryptedpassword")).thenReturn(false);

        RuntimeException runtimeException = assertThrows(RuntimeException.class , ()-> userService.login(loginRequest));

        assertEquals("Invalid password!", runtimeException.getMessage());
    }

    @Test
    void registerSuccess(){
        RegisterRequest request = new RegisterRequest();
        request.setUsername("Jyoti");
        request.setPassword("1234");
        request.setEmail("abc@gmail.com");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("Jyoti");
        savedUser.setPassword("encryptedpassword");
        savedUser.setEmail("abc@gmail.com");

        when(userRepository.existsByUsername("Jyoti")).thenReturn(false);

        when(userRepository.existsByEmail("abc@gmail.com")).thenReturn(false);

        when(passwordEncoder.encode("1234")).thenReturn("encryptedpassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        User result = userService.register(request);

        assertNotNull(result);
        assertEquals(1l, result.getId());
        assertEquals("Jyoti",result.getUsername());
        assertEquals("abc@gmail.com", result.getEmail());


        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).existsByUsername("Jyoti");
        verify(userRepository).existsByEmail("abc@gmail.com");
        verify(passwordEncoder).encode("1234");
        verify(userRepository).save(userCaptor.capture());
        verify(walletRepository).save(any(Wallet.class));

        User capturedUser = userCaptor.getValue();

        assertEquals("Jyoti", capturedUser.getUsername());
        assertEquals("abc@gmail.com", capturedUser.getEmail());
        assertEquals("encryptedpassword", capturedUser.getPassword());
    }

    @Test
    void registerUsernameAlreadyExists(){
        RegisterRequest request = new RegisterRequest();
        request.setUsername("Jyoti");
        request.setPassword("1234");
        request.setEmail("abc@gmail.com");


        when(userRepository.existsByUsername("Jyoti")).thenReturn(true);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> userService.register(request));

        assertEquals("Username already exists!", runtimeException.getMessage());

        verify(userRepository).findByUsername("Jyoti");

        verify(userRepository, never()).save(any(User.class));
        verify(walletRepository, never()).save(any(Wallet.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void registerEmailAlreadyExists(){
        RegisterRequest request = new RegisterRequest();
        request.setUsername("Jyoti");
        request.setPassword("1234");
        request.setEmail("abc@gmail.com");

        when(userRepository.existsByUsername("Jyoti"))
                .thenReturn(false);
        when(userRepository.existsByEmail("abc@gmail.com")).thenReturn(true);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> userService.register(request));

        assertEquals("Email already exists!", runtimeException.getMessage());


        verify(userRepository).existsByEmail("abc@gmail.com");
        verify(userRepository, never()).save(any(User.class));
        verify(walletRepository, never()).save(any(Wallet.class));
        verify(passwordEncoder, never()).encode(anyString());
    }
}
