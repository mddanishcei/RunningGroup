package com.springmvc.springmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.springmvc.springmvc.dtos.RegistrationDto;
import com.springmvc.springmvc.models.UserEntity;
import com.springmvc.springmvc.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(){
        return "login-page";
    }

    @GetMapping("/register")
    public String getRegisterForm(Model model){
        RegistrationDto user=new RegistrationDto();
        model.addAttribute("user", user);
        return "register-page";
    }

    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute RegistrationDto user,BindingResult result,Model model) {
        UserEntity existingUser=userService.findByEmail(user.getEmail());
        if(existingUser!=null && existingUser.getEmail()!=null && !existingUser.getEmail().isEmpty()){
            return "redirect:/register?fail";
        }

        UserEntity existingUsername=userService.findByUserName(user.getUsername());
        if(existingUsername!=null && existingUsername.getUsername()!=null && !existingUsername.getUsername().isEmpty()){
            return "redirect:/register?fail";
        }


        if(result.hasErrors()){
            model.addAttribute("user", user);
            return "register-page";
        }

        userService.saveUser(user);
        return "redirect:/clubs?success";
    }
    
}
