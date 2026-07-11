package com.jyotinath.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jyotinath.wallet.config.JwtUtil;
import com.jyotinath.wallet.controller.AuthController;
import com.jyotinath.wallet.dto.LoginRequest;
import com.jyotinath.wallet.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private UserService userService;

    @MockitoBean
    private JwtUtil jwtUtil;

        @Autowired
        private  ObjectMapper objectMapper;

        @Test
    void loginSuccess() throws Exception{
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername("Jyoti");
            loginRequest.setPassword("1234");

            when(userService.login(any(LoginRequest.class))).thenReturn("jwt-token");

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Login Successful!"))
                    .andExpect(jsonPath("$.data").value("jwt-token"));

            verify(userService).login(any(LoginRequest.class));

            
    }
}
