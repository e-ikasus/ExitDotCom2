package fr.eikasus.exitdotcom.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@RequestMapping("/security/")
@Controller public class SecurityController
{
	@RequestMapping("login")
	public String login()
	{
		return "misc/login";
	}

	@RequestMapping("logout")
	public String logout(HttpServletRequest request) throws ServletException
	{
		request.logout();

		return "redirect:/security/login";
	}
}
