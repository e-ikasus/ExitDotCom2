package fr.eikasus.exitdotcom.services.implementations;

import fr.eikasus.exitdotcom.dao.SchoolRepository;
import fr.eikasus.exitdotcom.entities.School;
import fr.eikasus.exitdotcom.services.interfaces.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service public class SchoolServiceImpl implements SchoolService
{
	private final SchoolRepository schoolRepository;

	public List<School> FindAll()
	{
		return schoolRepository.findByOrderByNameAsc();
	}
}
