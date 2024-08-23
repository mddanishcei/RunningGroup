package com.springmvc.springmvc.services.serviceimpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.springmvc.springmvc.dtos.ClubDto;
import com.springmvc.springmvc.dtos.EventDto;
import com.springmvc.springmvc.models.UserEntity;

public class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender javaMailSender;

    private UserEntity user1;
    private UserEntity user2;
    private ClubDto clubDto;
    private EventDto eventDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user1 = new UserEntity();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        user1.setUsername("user1");

        user2 = new UserEntity();
        user2.setId(2L);
        user2.setEmail("mddanishashraf10@gmail.com");
        user2.setUsername("user2");

        clubDto = new ClubDto();
        clubDto.setTitle("Test Club");
        clubDto.setContent("Test Content");

        eventDto = new EventDto();
        eventDto.setName("Test Event");
        eventDto.setType("Test Type");
        eventDto.setStartTime(LocalDateTime.now());
        eventDto.setEndTime(LocalDateTime.now());
    }

    @Test
    public void testSendingEmailToUserWithClub() {
        List<UserEntity> users = Arrays.asList(user1, user2);

        emailService.sendingEmailToUser(users, clubDto);

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testSendingEmailToUserWithEvent() {
        List<UserEntity> users = Arrays.asList(user1, user2);

        emailService.sendingEmailToUser(users, eventDto);

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testSendEmail() {
        emailService.sendEmail("user1@example.com", "Test Subject", "Test Text", "user1");

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}

