package com.springmvc.springmvc.services.serviceimpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.springmvc.springmvc.dtos.ClubDto;
import com.springmvc.springmvc.mappers.ClubMapper;
import com.springmvc.springmvc.models.Club;
import com.springmvc.springmvc.models.UserEntity;
import com.springmvc.springmvc.repository.ClubRepository;
import com.springmvc.springmvc.repository.UserRepository;

public class ClubServiceImplTest {

    @InjectMocks
    private ClubServiceImpl clubService;

    @Mock
    private ClubRepository clubRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    private UserEntity user;
    private Club club;
    private ClubDto clubDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new UserEntity();
        user.setId(1L);
        user.setUsername("testuser");

        club = new Club();
        club.setId(1L);
        club.setTitle("Test Club");
        club.setCreatedBy(user);

        clubDto = ClubMapper.mapToClubDto(club);
        setUpMockSecurityContext("testuser");
    }

    private void setUpMockSecurityContext(String username) {
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testFindAllClub() {
        List<Club> clubs = Arrays.asList(club);
        when(clubRepository.findAll()).thenReturn(clubs);

        List<ClubDto> result = clubService.findAll();

        assertEquals(1, result.size());
        assertEquals("Test Club", result.get(0).getTitle());
    }

    @Test
    public void testSaveClub() {
        when(userRepository.findFirstByUsername("testuser")).thenReturn(user);
        when(clubRepository.save(any(Club.class))).thenReturn(club);

        Club result = clubService.saveClub(clubDto);

        assertNotNull(result);
        assertEquals("Test Club", result.getTitle());
        verify(clubRepository, times(1)).save(any(Club.class));
    }

    @Test
    public void testFindClubById() {
        when(clubRepository.findById(1L)).thenReturn(Optional.of(club));

        ClubDto result = clubService.findById(1L);

        assertNotNull(result);
        assertEquals("Test Club", result.getTitle());
    }

    @Test
    public void testUpdateClub() {
        when(userRepository.findFirstByUsername("testuser")).thenReturn(user);

        clubService.update(clubDto);

        verify(clubRepository, times(1)).save(any(Club.class));
    }

    @Test
    public void testDelete() {
        clubService.delete(1L);

        verify(clubRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testSearchClubs() {
        List<Club> clubs = Arrays.asList(club);
        when(clubRepository.searchClub("Test")).thenReturn(clubs);

        List<ClubDto> result = clubService.searchClubs("Test");

        assertEquals(1, result.size());
        assertEquals("Test Club", result.get(0).getTitle());
    }
}
