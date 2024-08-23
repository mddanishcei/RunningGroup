package com.springmvc.springmvc.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.springmvc.springmvc.dtos.ClubDto;
import com.springmvc.springmvc.models.Club;
import com.springmvc.springmvc.models.UserEntity;
import com.springmvc.springmvc.security.SecurityUtil;
import com.springmvc.springmvc.services.UserService;
import com.springmvc.springmvc.services.serviceimpl.ClubServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;




@Controller
@RequiredArgsConstructor
public class ClubController implements BaseController<ClubDto>{

    private final ClubServiceImpl clubService;

    private final UserService userService;


    @GetMapping({"/clubs","/clubs/"})
    public String listAll(Model model){
        UserEntity user;
        List<ClubDto> clubDto=clubService.findAll();
        String username=SecurityUtil.getSessionUser();
        if(username!=null){
            user=userService.findByUserName(username);
        }else{
            user=new UserEntity();
        }
        model.addAttribute("user", user);
        model.addAttribute("clubs", clubDto);
        return "clubs-list";
    }

    @GetMapping("/clubs/{clubId}")
    public String detail(@PathVariable long clubId,Model model){
        UserEntity user=new UserEntity();
        ClubDto clubDto=clubService.findById(clubId);
        String username=SecurityUtil.getSessionUser();
        if(username!=null){
            user=userService.findByUserName(username);
            model.addAttribute("user", user);
        }
        model.addAttribute("user", user);
        model.addAttribute("club", clubDto);
        return "club-detail";
    }

    @GetMapping("/clubs/new")
    public String create(Model model){
        Club club=new Club();
        model.addAttribute("club", club);
        return "clubs-create";
    }

    @PostMapping("/clubs/new")
    public String save(@Valid @ModelAttribute("club") ClubDto clubDto,BindingResult result,Model model){
        if(result.hasErrors()){
            model.addAttribute("club", clubDto);
            return "clubs-create";
        }
        clubService.saveClub(clubDto);
        return "redirect:/clubs";
    }

    @GetMapping("/clubs/{clubId}/edit")
    public String edit(@PathVariable("clubId") long clubId,Model model){
        ClubDto club=clubService.findById(clubId);
        model.addAttribute("club", club);
        return "clubs-edit";
    } 

    @PostMapping("/clubs/{clubId}/edit")
    public String update(@PathVariable("clubId")long clubId,@Valid @ModelAttribute("club") ClubDto club,BindingResult result,Model model){
        if(result.hasErrors()){
            model.addAttribute("club", club);
            return "clubs-edit";
        }
        club.setId(clubId);
        clubService.update(club);
        return "redirect:/clubs";
    }

    @GetMapping("/clubs/{clubId}/delete")
    public String delete(@PathVariable long clubId) {
        clubService.delete(clubId);
        return "redirect:/clubs";
    }

    @GetMapping("/clubs/search")
    public String searchClub(@RequestParam("query") String query,Model model) {
        UserEntity user=new UserEntity();
        List<ClubDto> clubs=clubService.searchClubs(query);
        String username=SecurityUtil.getSessionUser();
        if(username!=null){
            user=userService.findByUserName(username);
            model.addAttribute("user", user);
        }
        model.addAttribute("user", user);
        model.addAttribute("clubs", clubs);
        return "clubs-list";
    }   
    
}
