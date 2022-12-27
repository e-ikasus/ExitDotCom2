package fr.eikasus.exitdotcom.controllers;

import fr.eikasus.exitdotcom.dao.StudentRepository;
import fr.eikasus.exitdotcom.entities.Exit;
import fr.eikasus.exitdotcom.entities.School;
import fr.eikasus.exitdotcom.entities.Student;
import fr.eikasus.exitdotcom.misc.SearchCriteria;
import fr.eikasus.exitdotcom.services.implementations.UserDetailsImpl;
import fr.eikasus.exitdotcom.services.interfaces.ExitService;
import fr.eikasus.exitdotcom.services.interfaces.SchoolService;
import fr.eikasus.exitdotcom.services.interfaces.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequestMapping({"/exit/", "/"})
@RequiredArgsConstructor
@Controller public class ExitController
{
	private final SchoolService schoolService;
	private final ExitService exitService;
	private final StudentRepository studentRepository;
	private final Tracer tracer;

	@RequestMapping({"list", "/"})
	public String list(@ModelAttribute SearchCriteria searchCriteria, HttpSession session, Model model, @RequestParam(defaultValue = "") String sort, @RequestParam(defaultValue = "1") int pageNumber, @RequestParam(defaultValue = "10") int pageSize)
	{
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Student authenticated = studentRepository.findById(userDetails.getIdentifier()).get();

		if (sort.length() == 0) sort = (String) session.getAttribute("sort");
		if (sort == null) sort = "name1";
		session.setAttribute("sort", sort);

		if (!searchCriteria.isValid()) searchCriteria = (SearchCriteria) session.getAttribute("SearchCriteria");
		if (searchCriteria == null) searchCriteria = new SearchCriteria();
		session.setAttribute("SearchCriteria", searchCriteria);

		int pageNumber0 = (pageNumber == 0) ? (0) : (pageNumber - 1);
		String columnName = sort.substring(0, sort.length() - 1);
		boolean columnOrder = (sort.substring(sort.length() - 1).compareTo("1") == 0);

		Page<Exit> exitPage = exitService.listByCriteria(authenticated, searchCriteria, pageNumber0, pageSize, columnName, columnOrder);

		List<School> schools = schoolService.FindAll();

		model.addAttribute("current", authenticated);
		model.addAttribute("exitsList", exitPage.getContent());
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("pageCount", new int[exitPage.getTotalPages()]);
		model.addAttribute("searchCriteria", searchCriteria);
		model.addAttribute("schools", schools);
		model.addAttribute("sort", sort);

		schools.forEach(school -> tracer.info(school.toString()));

		return "exit/list";
	}
}
