package com.bbbProject.demo.controllers;

import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.bbbProject.demo.models.User;


import com.bbbProject.demo.services.UserService;
import com.bbbProject.demo.validators.UserValidator;
//Why did it not work
@SpringBootApplication
@Controller
@RequestMapping("/user")
public class UserController {
	
	private UserService service;
	private UserValidator validator;
	
	public UserController(UserService service, UserValidator validator) {
		this.service = service;
		this.validator = validator;
	}
	
	@RequestMapping("/login")
	public String getLoginForm(@ModelAttribute("user") User user) {
		return "loginForm";
	}
	
	@PostMapping("/login")
	public String Login(
			@Valid User user, BindingResult result,
			RedirectAttributes redirectAttributes,
			HttpSession session
			) {
		
		if (result.hasErrors()) return "loginForm";
		
		user = this.service.login(user);
		
		if ( user == null ) {
			redirectAttributes.addFlashAttribute("errors", new ArrayList<String>(Arrays.asList("invalid credentials")));
			return "redirect:/user/login";
		}
		
		session.setAttribute("userId", user.getId());
		
		redirectAttributes.addFlashAttribute("messages", new ArrayList<String>(Arrays.asList("Welcome Back!")));
		return "redirect:/ideas/";
	}
	
	@GetMapping("/logout")
	public String Logout(HttpSession session) {
		session.invalidate();
		return "redirect:/user/login";
	}
	
	@RequestMapping("/register")
	public String getRegistrationForm(@ModelAttribute("user") User user) {
		return "registrationForm";
	}
	
	@PostMapping("/register")
	public String register(
			@Valid User user, BindingResult result,
			RedirectAttributes redirectAttributes
			) {
		this.validator.validate(user, result);											
		
		if (result.hasErrors()) return "registrationForm";
		
		user = this.service.register(user);
		
		if ( user == null ) {
			redirectAttributes.addFlashAttribute("errors", new ArrayList<String>(Arrays.asList("email address is not available")));
			return "redirect:/user/register";
		}
		
		
		// create session
		
		return "redirect:/user/login";
	}

}
