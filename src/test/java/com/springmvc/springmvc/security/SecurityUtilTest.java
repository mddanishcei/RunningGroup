package com.springmvc.springmvc.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtilTest {

    private Authentication authentication;
    private SecurityContext securityContext;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authentication= mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
    }

    @AfterEach
    void endUp(){
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testGetSessionUser_Authenticated() {
        when(authentication.getName()).thenReturn("testuser");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        String username = SecurityUtil.getSessionUser();
        assertEquals("testuser", username);
    }

    @Test
    public void testGetSessionUser_Anonymous() {
        Authentication authentication = mock(AnonymousAuthenticationToken.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext); 
        String username = SecurityUtil.getSessionUser();
        assertNull(username);  
    }

    @Test
    public void testGetSessionUser_NoAuthentication() {
        when(securityContext.getAuthentication()).thenReturn(null); 
        SecurityContextHolder.setContext(securityContext); 
        String username = SecurityUtil.getSessionUser();
        assertNull(username);
    }
}
