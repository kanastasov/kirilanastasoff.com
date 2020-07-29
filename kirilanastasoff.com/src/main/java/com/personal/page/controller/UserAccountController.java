package com.personal.page.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.personal.page.service.UserAccountService;

@Controller
public class UserAccountController {

	@Autowired
	private UserAccountService userAccountService;
	
	@GetMapping("/")
	public String showAllAcounts(Model model) {
		model.addAttribute("listUserAccounts", userAccountService.getAllUserAccounts());
		return "index";

	}
}
