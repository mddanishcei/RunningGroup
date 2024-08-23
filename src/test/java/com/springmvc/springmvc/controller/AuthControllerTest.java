package com.springmvc.springmvc.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.springmvc.springmvc.models.UserEntity;
import com.springmvc.springmvc.services.UserService;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @InjectMocks
    private AuthController authController;
    
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        mockMvc=MockMvcBuilders.standaloneSetup(authController).build();
    }
    

    @Test
    public void testLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
               .andExpect(status().isOk())
               .andExpect(view().name("login-page"));
    }

    @Test
    public void testGetRegisterForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
               .andExpect(view().name("register-page"))
               .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(null);
        when(userService.findByUserName(anyString())).thenReturn(null);

        mockMvc.perform(post("/register/save")
        .param("email", "a@maiil.com")
        .param("username", "mddanish")
        .param("password", "password"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/clubs?success"));
    }

    @Test
    public void testRegisterFailEmailExists() throws Exception {
        UserEntity existingUser = new UserEntity();
        existingUser.setEmail("test@example.com");
        when(userService.findByEmail(anyString())).thenReturn(existingUser);

        mockMvc.perform(post("/register/save")
                .param("email", "test@example.com")
                .param("username", "testuser")
                .param("password", "password"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/register?fail"));
    }

    @Test
    public void testRegisterFailUsernameExists() throws Exception {
        UserEntity existingUser = new UserEntity();
        existingUser.setUsername("testuser");
        when(userService.findByUserName(anyString())).thenReturn(existingUser);

        mockMvc.perform(post("/register/save")
                .param("email", "newemail@example.com")
                .param("username", "testuser")
                .param("password", "password"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/register?fail"));
    }

    @Test
    public void testRegisterFailValidationErrors() throws Exception {
        mockMvc.perform(post("/register/save")
                .param("email", "")
                .param("username", "testuser")
                .param("password", "password"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("user"))
               .andExpect(view().name("register-page"));
    }
}
