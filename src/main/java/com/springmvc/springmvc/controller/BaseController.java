package com.springmvc.springmvc.controller;


import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;


import jakarta.validation.Valid;

public interface BaseController<T> {
    String listAll(Model model);
    String detail(long id, Model model);
    String update(long id, @Valid T dto, BindingResult result, Model model);
    String delete(long id);   
}

