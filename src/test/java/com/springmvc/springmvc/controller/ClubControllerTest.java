package com.springmvc.springmvc.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.springmvc.springmvc.dtos.ClubDto;
import com.springmvc.springmvc.models.Club;
import com.springmvc.springmvc.models.UserEntity;
import com.springmvc.springmvc.services.UserService;
import com.springmvc.springmvc.services.serviceimpl.ClubServiceImpl;
import com.springmvc.springmvc.services.serviceimpl.EmailService;
import com.springmvc.springmvc.security.SecurityUtil;


@WebMvcTest(controllers = ClubController.class)
public class ClubControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClubServiceImpl clubService;

    @MockBean
    private UserService userService;

    @MockBean
    private EmailService emailService;


    @MockBean
    private Authentication authentication;

    @InjectMocks
    private ClubController clubController;

    private ClubDto clubDto;
    private Club club;
    private UserEntity user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc=MockMvcBuilders.standaloneSetup(clubController).build();

        clubDto = new ClubDto();
        clubDto.setId(1L);
        clubDto.setTitle("Test Club");
        clubDto.setContent("Test Content");
        clubDto.setPhotoUrl("photo.jpg");

        club = new Club();
        club.setId(1L);
        club.setTitle("Test Club");
        club.setContent("Test Content");

        user = new UserEntity();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");

        setUpMockSecurityContext("testuser");
    }

    private void setUpMockSecurityContext(String username) {
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testListClubs() throws Exception {
        List<ClubDto> clubDtos = Arrays.asList(clubDto);

        when(clubService.findAll()).thenReturn(clubDtos);
        when(SecurityUtil.getSessionUser()).thenReturn("testuser");
        when(userService.findByUserName("testuser")).thenReturn(user);

        mockMvc.perform(get("/clubs"))
                .andExpect(status().isOk())
                .andExpect(view().name("clubs-list"))
                .andExpect(model().attributeExists("clubs"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testClubDetail() throws Exception {
        when(clubService.findById(anyLong())).thenReturn(clubDto);
        when(SecurityUtil.getSessionUser()).thenReturn("testuser");
        when(userService.findByUserName("testuser")).thenReturn(user);

        mockMvc.perform(get("/clubs/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("club-detail"))
                .andExpect(model().attributeExists("club"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testCreateClubNew() throws Exception {
        mockMvc.perform(get("/clubs/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("clubs-create"))
                .andExpect(model().attributeExists("club"));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testSaveClubWithValidData() throws Exception {

        mockMvc.perform(post("/clubs/new")
                .flashAttr("club", clubDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/clubs"));

        verify(clubService).saveClub(clubDto);
        verify(userService).findAll();
        verify(emailService).sendingEmailToUser(any(List.class), any(ClubDto.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    void testSaveClubWithValidationErrors() throws Exception {
        ClubDto clubDto = new ClubDto();

        mockMvc.perform(post("/clubs/new")
                .flashAttr("club", clubDto))
                .andExpect(status().isOk())
                .andExpect(view().name("clubs-create"))
                .andExpect(model().attributeHasErrors("club"))
                .andExpect(model().attribute("club", clubDto));

        
        verify(clubService, never()).saveClub(any(ClubDto.class));
        verify(userService, never()).findAll();
        verify(emailService, never()).sendingEmailToUser(any(List.class), any(ClubDto.class));
    }

    @Test
    public void testEditClubForm() throws Exception {
        when(clubService.findById(anyLong())).thenReturn(clubDto);

        mockMvc.perform(get("/clubs/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("clubs-edit"))
                .andExpect(model().attributeExists("club"));
    }

    @Test
    public void testUpdateClubWithValidationErrors() throws Exception {
        ClubDto clubDto = new ClubDto();

        mockMvc.perform(post("/clubs/1/edit")
                .flashAttr("club", clubDto))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("club"))
                .andExpect(model().attribute("club", clubDto))
                .andExpect(view().name("clubs-edit"));
    }

    @Test
    public void testUpdateClubWithOutValidationErrors() throws Exception {

        mockMvc.perform(post("/clubs/1/edit")
                .flashAttr("club", clubDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clubs"));
    }

    


    @Test
    public void testDeleteClub() throws Exception {
        mockMvc.perform(get("/clubs/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/clubs"));

        verify(clubService, times(1)).delete(anyLong());
    }

    @Test
    public void testSearchClub() throws Exception {
        List<ClubDto> clubs = Arrays.asList(clubDto);

        when(clubService.searchClubs(anyString())).thenReturn(clubs);
        when(SecurityUtil.getSessionUser()).thenReturn("testuser");
        when(userService.findByUserName("testuser")).thenReturn(user);

        mockMvc.perform(get("/clubs/search")
                .param("query", "Test"))
                .andExpect(status().isOk())
                .andExpect(view().name("clubs-list"))
                .andExpect(model().attributeExists("clubs"))
                .andExpect(model().attributeExists("user"));
    }

    
}
