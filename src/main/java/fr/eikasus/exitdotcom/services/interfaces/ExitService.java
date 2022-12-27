package fr.eikasus.exitdotcom.services.interfaces;

import fr.eikasus.exitdotcom.entities.Exit;
import fr.eikasus.exitdotcom.entities.Student;
import fr.eikasus.exitdotcom.misc.SearchCriteria;
import org.springframework.data.domain.Page;

/**
 * Interface used to handle exits.
 * <p></p>
 * This interface define all the methods required to handle exits correctly. An
 * implementation of this service is necessary.
 *
 * @see #list(int, int)
 * @see #listByCriteria(Student, SearchCriteria, int, int, String, boolean)
 */

public interface ExitService
{
	// Retrieve all exits in an unordered manner.
	Page<Exit> list(int pageNumber, int pageSize);

	// Retrieve an ordered list of exits by criteria.
	Page<Exit> listByCriteria(Student student, SearchCriteria searchCriteria, int pageNumber, int pageSize, String columnName, boolean ascending);
}
