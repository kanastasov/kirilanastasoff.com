package com.personal.page.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.lowagie.text.DocumentException;
import com.personal.page.exception.UserAccountException;
import com.personal.page.model.UserAccount;
import com.personal.page.model.dto.UserAccountDto;
import com.personal.page.service.EmailService;
import com.personal.page.service.PdfServiceImp;
import com.personal.page.service.UserAccountService;

@Controller
public class UserAccountController {

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private EmailService emailService;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncode;

	@Autowired
	private PdfServiceImp pdfService;

	@Autowired
	public UserAccountController(UserAccountService userAccountService, EmailService emailService,
			BCryptPasswordEncoder bCryptPasswordEncode, PdfServiceImp pdfService) {
		super();
		this.userAccountService = userAccountService;
		this.emailService = emailService;
		this.bCryptPasswordEncode = bCryptPasswordEncode;
		this.pdfService = pdfService;
	}
	
    @ModelAttribute("userdto")
    public UserAccountDto userRegistrationDto() {
        return new UserAccountDto();
    }

	@GetMapping("/")
	public String showAllAcounts(Model model) {
		model.addAttribute("listUserAccounts", userAccountService.getAllUserAccounts());
		return "index";
	}

	// create model attribute to bind form data
	@GetMapping("/showUserAccountForm")
	public String showUserAccountForm(Model model) {
		UserAccount userAccount = new UserAccount();
		model.addAttribute("userAccount", userAccount);
		return "newUserAccount";
	}

	@PostMapping("/saveUserAccount")
	public String saveAccount(@ModelAttribute("userAccount") @Valid UserAccount account, BindingResult bindingResult) {
		UserAccount user = userAccountService.findUserAccountByEmail(account.getEmail());
		if (user != null) {
			bindingResult.rejectValue("email", "There is already an account with this email");
		}
		if (bindingResult.hasErrors()) {
			return "newUserAccount";
		}
		userAccountService.saveUserAccount(account);
		return "redirect:/";
	}

	@GetMapping("/edit/{id}")
	public String showUpdateForm(@PathVariable("id") long id, Model model) {
		UserAccount userAccount;
		try {
			userAccount = userAccountService.getUserAccountById(id);
			model.addAttribute("userAccount", userAccount);
		} catch (UserAccountException e) {
			e.printStackTrace();
		}
		return "updateUserAccount";
	}

	// todo set username to null when update user
	@PostMapping("/update/{id}")
	public String updateUserAccount(@PathVariable("id") long id, @Valid UserAccount userAccount, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			return "updateUserAccount";
		}
		userAccountService.saveUserAccount(userAccount);
		model.addAttribute("userAccount", userAccountService.getAllUserAccounts());
		return "redirect:/";
	}

	@GetMapping("/deleteAccount/{id}")
	public String deleteAccount(@PathVariable(value = "id") long id) {
		userAccountService.deleteUserAccountById(id);
		return "redirect:/";
	}

	@GetMapping("/login")
	public String login(Model model) {
		UserAccount userAccount = new UserAccount();
		model.addAttribute("userAccount", userAccount);
		return "login";
	}

	@GetMapping(value = "/admin/home")
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserAccount user = userAccountService.findUserByUsername(auth.getName());
		modelAndView.addObject("username", "Welcome " + user.getUsername() + "/" + user.getFirstName() + " "
				+ user.getLastName() + " (" + user.getEmail() + ")");
		modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
		modelAndView.setViewName("admin/home");
		return modelAndView;
	}

	@GetMapping("/download-pdf")
	public void downloadPDFResource(HttpServletResponse response) {
		try {
			Path file = Paths.get(pdfService.generatePdf().getAbsolutePath());
			if (Files.exists(file)) {
				response.setContentType("application/pdf");
				response.addHeader("Content-Disposition", "attachment; filename=" + file.getFileName());
				Files.copy(file, response.getOutputStream());
				response.getOutputStream().flush();
			}
		} catch (DocumentException | IOException ex) {
			ex.printStackTrace();
		}
	}

	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	public ResponseEntity<UserAccount> getUserAccountById(@PathVariable("id") long id) throws UserAccountException {
		UserAccount userAccount = userAccountService.getUserAccountById(id);
		if (userAccount == null || userAccount.getId() <= 0) {
			throw new UserAccountException("UserAccount doesn´t exist");
		}
		return new ResponseEntity<UserAccount>(userAccountService.getUserAccountById(id), HttpStatus.OK);
	}
}