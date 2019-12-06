package com.docker.javaweb.controller;

import javax.validation.Valid;

import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;

import com.docker.javaweb.model.User;
import com.docker.javaweb.model.UserLogin;
import com.docker.javaweb.service.UserService;

@Controller
@SessionAttributes("user")
public class UserController {

	@Autowired
	private UserService userService;

	String baseUri = System.getenv("BASEURI");

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signup(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "signup";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "signup";
		} else if (userService.userExists(user.getUserName())) {
			model.addAttribute("message", "User Name exists. Try another user name");
			return "signup";
		} else {
			JSONObject userData = new JSONObject();
			userData.put("userName", user.getUserName());
			userData.put("password", user.getPassword());
			userData.put("firstName", user.getFirstName());
			userData.put("lastName", user.getLastName());

			try {
				SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
				Date parsedDate = sdf.parse(user.getWorkshopDate().toString());
				SimpleDateFormat dob = new SimpleDateFormat("yyyy-MM-dd");
				userData.put("workshopDate", dob.format(parsedDate));
			} catch (ParseException e) {
				System.out.println(e);
			}
			userData.put("emailAddress", user.getEmailAddress());
			RestTemplate rt = new RestTemplate();

			try {
				HttpHeaders headers = new HttpHeaders();
				headers.add("Content-Type", "application/json");
				HttpEntity<String> entity = new HttpEntity<String>(userData.toString(), headers);
				rt.postForObject(baseUri, entity, String.class);
			} catch (Exception e) {
				System.out.println(userData.toString());
				System.out.println(e);
			}
			// model.addAttribute("message", "Saved user details");
			return "redirect:login.html";
		}
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		UserLogin userLogin = new UserLogin();
		model.addAttribute("userLogin", userLogin);
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@Valid @ModelAttribute("userLogin") UserLogin userLogin, BindingResult result) {
		if (result.hasErrors()) {
			return "login";
		} else {
			boolean found = false;

			try {
				found = userService.findByLogin(userLogin.getUserName(), userLogin.getPassword());
			} catch (org.springframework.web.client.HttpClientErrorException e) {
				if (e.getStatusCode() == org.springframework.http.HttpStatus.NOT_FOUND) {
					System.out.println("User not found.");
				} else {
					throw e;
				}
			}

			if (found) {
				return "success";
			} else {
				return "failure";
			}
		}

	}
}
