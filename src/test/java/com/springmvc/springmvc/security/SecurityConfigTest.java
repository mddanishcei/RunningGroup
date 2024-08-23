package com.springmvc.springmvc.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void testPublicEndpoints() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
        
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/clubs"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/css/styles.css"))
                .andExpect(status().isOk());
        
        mockMvc.perform(get("/js/scripts.js"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/register/save"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testAuthenticatedEndpoints() throws Exception {
        mockMvc.perform(get("/events"))
                .andExpect(status().isOk());
    }

    @Test
    public void testLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login-page")); 
    }

    @Test
    public void testLogout() throws Exception {
        mockMvc.perform(post("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDefaultSuccessUrl() throws Exception {
        mockMvc.perform(post("/login").param("username", "user").param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clubs"));
    }

    @Test
    public void testLoginFailure() throws Exception {
        mockMvc.perform(post("/login").param("username", "user").param("password", "wrongpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=true"));
    }
}
