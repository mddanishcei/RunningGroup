package com.springmvc.springmvc.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import com.springmvc.springmvc.dtos.EventDto;
import com.springmvc.springmvc.models.Club;
import com.springmvc.springmvc.models.UserEntity;
import com.springmvc.springmvc.security.SecurityUtil;
import com.springmvc.springmvc.services.UserService;
import com.springmvc.springmvc.services.serviceimpl.EmailService;
import com.springmvc.springmvc.services.serviceimpl.EventServiceImp;

@WebMvcTest(controllers = EventController.class)
public class EventControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventServiceImp eventService;

    @MockBean
    private UserService userService;

    @MockBean
    private EmailService emailService;

    @MockBean
    private Authentication authentication;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private EventController eventController;

    private EventDto eventDto;
    private UserEntity user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();

        eventDto = new EventDto();
        eventDto.setId(1l);
        eventDto.setName("eventDto");
        eventDto.setPhotoUrl("event.jpg");
        eventDto.setType("type");
        eventDto.setStartTime(LocalDateTime.now());
        eventDto.setEndTime(LocalDateTime.now());

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
    public void testEventList() throws Exception {
        List<EventDto> eventDtos = Arrays.asList(eventDto);

        when(eventService.findAll()).thenReturn(eventDtos);
        when(SecurityUtil.getSessionUser()).thenReturn("testuser");
        when(userService.findByUserName("testuser")).thenReturn(user);

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(view().name("events-list"))
                .andExpect(model().attributeExists("events"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testViewEvent() throws Exception {
        when(eventService.findById(anyLong())).thenReturn(eventDto);
        when(SecurityUtil.getSessionUser()).thenReturn("testuser");
        when(userService.findByUserName("testuser")).thenReturn(user);

        mockMvc.perform(get("/events/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("evetns-detail"))
                .andExpect(model().attributeExists("event"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testCreateEventForm() throws Exception {
        mockMvc.perform(get("/events/1/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("events-create"))
                .andExpect(model().attributeExists("event"))
                .andExpect(model().attributeExists("clubId"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCreateEventWithValidData() throws Exception {

        mockMvc.perform(post("/events/{clubId}/new", 1L)
                .flashAttr("event", eventDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clubs/1"));

        verify(eventService).createEvent(1l, eventDto);
        verify(userService).findAll();
        verify(emailService).sendingEmailToUser(any(List.class), any(EventDto.class));
    }

    @Test
    public void testCreateEventWithoutValidData() throws Exception {
        EventDto eventDto = new EventDto();
        eventDto.setId(1L);
        eventDto.setName("");

        mockMvc.perform(post("/events/{clubId}/new", 1L)
                .flashAttr("event", eventDto))
                .andExpect(status().isOk())
                .andExpect(view().name("events-create"))
                .andExpect(model().attributeHasFieldErrors("event", "name", "startTime", "endTime", "type", "photoUrl"))
                .andExpect(model().attribute("event", eventDto));

        verify(eventService, never()).createEvent(1L, eventDto);
        verify(userService, never()).findAll();
    }

    @Test
    public void testDeleteEvent() throws Exception {
        mockMvc.perform(get("/events/{eventId}/delete", 1l))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events"));

        verify(eventService).delete(1l);
    }

    @Test
    public void testEditEventFrom() throws Exception {
        Long eventId = 1L;
        EventDto event = new EventDto();
        event.setId(eventId);
        event.setName("Test Event");

        when(eventService.findById(eventId)).thenReturn(event);

        mockMvc.perform(get("/events/{eventId}/edit", eventId))
                .andExpect(status().isOk())
                .andExpect(view().name("events-edit"))
                .andExpect(model().attribute("event", event));
    }

    @Test
    public void testUpdateEventWithErrors() throws Exception {
        EventDto event = new EventDto();
        event.setId(1L);
        event.setName("Test Event");

        when(bindingResult.hasErrors()).thenReturn(true);

        mockMvc.perform(post("/events/1/edit")
                .flashAttr("event", event)
                .param("eventId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("events-list"));
    }

    @Test
    public void testUpdateEventSuccess() throws Exception {
        EventDto event = EventDto.builder()
                .id(1L)
                .name("Test Event")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(1))
                .type("Conference")
                .photoUrl("http://example.com/photo.jpg")
                .build();

        EventDto existingEvent = EventDto.builder()
                .id(1L)
                .name("Existing Event")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(1))
                .type("Workshop")
                .photoUrl("http://example.com/photo.jpg")
                .createdOn(LocalDateTime.now())
                .updatedON(LocalDateTime.now())
                .club(new Club())
                .build();

        when(bindingResult.hasErrors()).thenReturn(false);
        when(eventService.findById(1L)).thenReturn(existingEvent);

        mockMvc.perform(post("/events/1/edit")
                .flashAttr("event", event))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events"));

        verify(eventService).update(event);
    }

}
