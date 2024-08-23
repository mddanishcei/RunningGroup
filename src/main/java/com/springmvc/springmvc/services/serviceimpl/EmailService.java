package com.springmvc.springmvc.services.serviceimpl;

import java.util.List;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.springmvc.springmvc.dtos.ClubDto;
import com.springmvc.springmvc.dtos.EventDto;
import com.springmvc.springmvc.models.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    
    private final JavaMailSender javaMailSender;

    @Async
    public void sendingEmailToUser(List<UserEntity> users,ClubDto club){

        String msg="A new club has been opened in your Area. the name of club is "+club.getTitle()+" and this club will provide these services "+club.getContent()+".";

        for (UserEntity userEntity : users) {
            if ("mddanishashraf10@gmail.com".equals(userEntity.getEmail())) {
                continue;
            }
            sendEmail(userEntity.getEmail(), club.getTitle(), msg, userEntity.getUsername());
        }
    }

    @Async
    public void sendingEmailToUser(List<UserEntity> users,EventDto event){
        
        String msg="A new Event is going on in your area of type "+event.getType()+". and the starting date of event is "+event.getStartTime()+" and this event is going to end on "+event.getEndTime()+".";

        for (UserEntity userEntity : users) {
            if ("mddanishashraf10@gmail.com".equals(userEntity.getEmail())) {
                continue;
            }
            sendEmail(userEntity.getEmail(), event.getName(), msg, userEntity.getUsername());
        }
    }
    
    public void sendEmail(String to,String subject,String text,String username){
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setFrom("no-reply@gmail.com");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText("Hi,"+username+"\n"+text);

        javaMailSender.send(simpleMailMessage);
    }
}
