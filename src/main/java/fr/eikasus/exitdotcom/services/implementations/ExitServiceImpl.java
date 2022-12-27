package fr.eikasus.exitdotcom.services.implementations;

import fr.eikasus.exitdotcom.dao.ExitRepository;
import fr.eikasus.exitdotcom.entities.Exit;
import fr.eikasus.exitdotcom.entities.Student;
import fr.eikasus.exitdotcom.misc.SearchCriteria;
import fr.eikasus.exitdotcom.services.interfaces.ExitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Implementation of the exits service.
 * <p></p>
 * This class is the implementation of the service that manage exits. It
 * contains the business logic.
 *
 * @see #list(int, int)
 * @see #listByCriteria(Student, SearchCriteria, int, int, String, boolean)
 */

@RequiredArgsConstructor
@Service public class ExitServiceImpl implements ExitService
{
	/* ************* */
	/* Class members */
	/* ************* */

	// Repository used to access exits management methods.
	private final ExitRepository exitRepository;

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	/**
	 * Retrieve all exits in an unordered manner.
	 * <p></p>
	 * This method retrieve the exits at the specified page. The size of a page
	 * define the number of exits per page.
	 *
	 * @param pageNumber Number of the page to retrieve.
	 * @param pageSize   Size of a page.
	 *
	 * @return List of exits.
	 */

	public Page<Exit> list(int pageNumber, int pageSize)
	{
		return exitRepository.findAll(PageRequest.of(pageNumber, pageSize));
	}

	/**
	 * Retrieve an ordered list of exits by criteria.
	 * <p></p>
	 * This method retrieve the exits at the specified page that correspond to the
	 * supplied filters in parameter. The size of a page define the number of
	 * exits per page. The supplied student represent the connected user.
	 *
	 * @param student        Current connected student.
	 * @param searchCriteria Criteria to use for filtering.
	 * @param pageNumber     Number of the page to retrieve.
	 * @param pageSize       Size of a page.
	 * @param columnName     Name of the column to use for sorting.
	 * @param ascending      Order of the sort.
	 *
	 * @return List of exits
	 */

	public Page<Exit> listByCriteria(Student student, SearchCriteria searchCriteria, int pageNumber, int pageSize, String columnName, boolean ascending)
	{
		return exitRepository.findByCriteria(student, searchCriteria, PageRequest.of(pageNumber, pageSize), columnName, ascending);
	}
}
