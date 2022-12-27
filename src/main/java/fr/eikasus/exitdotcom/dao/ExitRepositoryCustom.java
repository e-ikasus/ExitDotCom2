package fr.eikasus.exitdotcom.dao;

import fr.eikasus.exitdotcom.entities.Exit;
import fr.eikasus.exitdotcom.entities.Student;
import fr.eikasus.exitdotcom.misc.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Exit custom repository interface.
 * <p></p>
 * This interface define the additional methods used to manage exits and that
 * are not handled by the framework.
 *
 * @see #findByCriteria(Student, SearchCriteria, Pageable, String, boolean)
 */

public interface ExitRepositoryCustom
{
	// Retrieve an ordered list of exits by criteria.
	Page<Exit> findByCriteria(Student student, SearchCriteria searchCriteria, Pageable page, String columnName, boolean ascending);
}
