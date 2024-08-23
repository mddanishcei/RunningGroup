package com.springmvc.springmvc.services.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.springmvc.springmvc.dtos.ClubDto;
import com.springmvc.springmvc.mappers.ClubMapper;
import com.springmvc.springmvc.models.Club;
import com.springmvc.springmvc.models.UserEntity;
import com.springmvc.springmvc.repository.ClubRepository;
import com.springmvc.springmvc.repository.UserRepository;
import com.springmvc.springmvc.security.SecurityUtil;
import com.springmvc.springmvc.services.BaseService;
import com.springmvc.springmvc.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClubServiceImpl implements BaseService<ClubDto,Long>{

    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final EmailService emailService;


    @Override
    public List<ClubDto> findAll() {
        List<Club> lclub=clubRepository.findAll();
        return lclub.stream().map((club)->ClubMapper.mapToClubDto(club)).collect(Collectors.toList());
    }

    


    
    public Club saveClub(ClubDto clubDto) {
        String username=SecurityUtil.getSessionUser();
        UserEntity user=userRepository.findFirstByUsername(username);
        Club club=ClubMapper.mapToClub(clubDto);
        club.setCreatedBy(user);
        Club c=clubRepository.save(club);
        List<UserEntity> users=userService.findAll();
        emailService.sendingEmailToUser(users,clubDto);
        return c;
    }


    @Override
    public ClubDto findById(Long clubId) {
        Club club=clubRepository.findById(clubId).get();
        return ClubMapper.mapToClubDto(club);
    }


    @Override
    public void update(ClubDto clubDto) {
        String username=SecurityUtil.getSessionUser();
        UserEntity user=userRepository.findFirstByUsername(username);
        Club club=ClubMapper.mapToClub(clubDto);
        club.setCreatedBy(user);
        clubRepository.save(club);
    }

    


    @Override
    public void delete(Long clubId) {
        clubRepository.deleteById(clubId);
    }


    
    public List<ClubDto> searchClubs(String query) {
        List<Club> clubs=clubRepository.searchClub(query);
        return clubs.stream().map(club->ClubMapper.mapToClubDto(club)).collect(Collectors.toList());
    }
}

