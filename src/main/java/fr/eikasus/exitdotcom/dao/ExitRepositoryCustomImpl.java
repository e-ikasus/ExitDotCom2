package fr.eikasus.exitdotcom.dao;

import fr.eikasus.exitdotcom.entities.Exit;
import fr.eikasus.exitdotcom.entities.StateCode;
import fr.eikasus.exitdotcom.entities.Student;
import fr.eikasus.exitdotcom.misc.SearchCriteria;
import fr.eikasus.exitdotcom.services.interfaces.Tracer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

/**
 * Exit custom repository.
 * <p></p>
 * This class is the implementation of the
 * {@link ExitRepositoryCustom ExitRepositoryCustom} interface. It contains all
 * the additional methods that are not supplied by the framework.
 *
 * @see #findByCriteria(Student, SearchCriteria, Pageable, String, boolean)
 */

public class ExitRepositoryCustomImpl implements ExitRepositoryCustom
{
	/* ************* */
	/* Class members */
	/* ************* */

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private Tracer tracer;

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	/**
	 * Retrieve an ordered list of exits by criteria.
	 * <p></p>
	 * This method retrieve an ordered list of exits at the specified page. The
	 * order and the specified exits are defined in the searchCriteria parameter.
	 * The student represent the current connected user (but this is not
	 * necessary) and is used for some filters.
	 *
	 * @param student        Current connected student.
	 * @param searchCriteria Criteria to use for filtering.
	 * @param page           Number of the page to retrieve.
	 * @param columnName     Name of the column to use for sorting.
	 * @param ascending      Order of the sort.
	 *
	 * @return Ordered list of exits
	 */

	public Page<Exit> findByCriteria(@NotNull Student student, @NotNull SearchCriteria searchCriteria, @NotNull Pageable page, String columnName, boolean ascending)
	{
		Predicate predicate, predicateCount, where = null, whereCount = null;
		Long number;

		// Retrieve the criteriaBuilder.
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

		// Create a new query that will output an exit.
		CriteriaQuery<Exit> query = criteriaBuilder.createQuery(Exit.class);

		// Create an instance that represent the "from" clause.
		Root<Exit> exit = query.from(Exit.class);

		if (columnName != null)
		{
			Path<?> path = exit.get(columnName);

			try
			{
				if (columnName.compareTo("organiser") == 0) path = path.get("username");
				else if (columnName.compareTo("state") == 0) path = path.get("code");

				query.orderBy((ascending) ? (criteriaBuilder.asc(path)) : (criteriaBuilder.desc(path)));
			}
			catch (IllegalArgumentException e)
			{
				// Nothing to do.
			}
		}

		// Create the query for retrieving number of exits.
		CriteriaQuery<Long> count = criteriaBuilder.createQuery(Long.class);

		// Create an instance that represent the "from" clause.
		Root<Exit> exitCount = count.from(Exit.class);

		// If the exits organised by the supplied student need to be retrieved.
		if (searchCriteria.isOrganiser())
		{
			// Create the "exit.organiser = student" predicate.
			where = criteriaBuilder.equal(exit.get("organiser"), student);
			whereCount = criteriaBuilder.equal(exitCount.get("organiser"), student);
		}

		// If the exits at which the supplied student is registered need to be
		// retrieved.
		if (searchCriteria.isSubscribe())
		{
			// Create the predicate that force the exit to be in the student registered
			// exits list.
			predicate = exit.in(student.getRegisteredExits());
			predicateCount = exitCount.in(student.getRegisteredExits());

			// Create another predicate for a logical OR if there is already one.
			where = (where == null) ? (predicate) : criteriaBuilder.or(where, predicate);
			whereCount = (whereCount == null) ? (predicateCount) : criteriaBuilder.or(whereCount, predicateCount);
		}

		// If the exits at which the supplied student is not registered need to be
		// retrieved.
		if (searchCriteria.isUnsubscribe())
		{
			// Create the predicate that force the exit not to be in the student
			// registered exits list.
			predicate = exit.in(student.getRegisteredExits()).not();
			predicateCount = exitCount.in(student.getRegisteredExits()).not();

			// Create another predicate for a logical OR if there is already one.
			where = (where == null) ? (predicate) : criteriaBuilder.or(where, predicate);
			whereCount = (whereCount == null) ? (predicateCount) : criteriaBuilder.or(whereCount, predicateCount);
		}

		// If the ended exits need to be retrieved.
		if (searchCriteria.isPast())
		{
			// Create the "exit.state.code = EN" predicate.
			predicate = criteriaBuilder.equal(exit.get("state").get("code"), StateCode.EN.name());
			predicateCount = criteriaBuilder.equal(exitCount.get("state").get("code"), StateCode.EN.name());

			// Create another predicate for a logical OR if there is already one.
			where = (where == null) ? (predicate) : criteriaBuilder.or(where, predicate);
			whereCount = (whereCount == null) ? (predicateCount) : criteriaBuilder.or(whereCount, predicateCount);
		}

		// If there is a filter on the exits name.
		if (searchCriteria.getContent() != null)
		{
			// Create the "exit.name like %keyword%" predicate.
			predicate = criteriaBuilder.like(exit.get("name"), "%" + searchCriteria.getContent() + "%");
			predicateCount = criteriaBuilder.like(exitCount.get("name"), "%" + searchCriteria.getContent() + "%");

			// Create another predicate for a logical AND if there is already one.
			where = (where == null) ? (predicate) : criteriaBuilder.and(where, predicate);
			whereCount = (whereCount == null) ? (predicateCount) : criteriaBuilder.and(whereCount, predicateCount);
		}

		// If exits from a particular school need to be retrieved.
		if (searchCriteria.getSchool() != null)
		{
			// Create the "exit.school = school" predicate.
			predicate = criteriaBuilder.equal(exit.get("school"), searchCriteria.getSchool());
			predicateCount = criteriaBuilder.equal(exitCount.get("school"), searchCriteria.getSchool());

			// Create another predicate for a logical AND if there is already one.
			where = (where == null) ? (predicate) : criteriaBuilder.and(where, predicate);
			whereCount = (whereCount == null) ? (predicateCount) : criteriaBuilder.and(whereCount, predicateCount);
		}

		// Construct the query for retrieving the total number of found exits.
		count.select(criteriaBuilder.count(exitCount));
		if (whereCount != null) count.where(whereCount);

		// Compute the number of exits.
		number = entityManager.createQuery(count).getSingleResult();

		tracer.info(number.toString());

		// Construct the query.
		query.select(exit);
		if (where != null) query.where(where);

		TypedQuery<Exit> typedQuery = entityManager.createQuery(query);
		typedQuery.setMaxResults(page.getPageSize());
		typedQuery.setFirstResult(page.getPageNumber() * page.getPageSize());

		List<Exit> result = typedQuery.getResultList();

		return new PageImpl<Exit>(result, page, number);
	}
}
