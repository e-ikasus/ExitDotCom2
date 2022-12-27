package fr.eikasus.exitdotcom.dao;

import fr.eikasus.exitdotcom.entities.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository public interface SchoolRepository extends JpaRepository<School, Long>
{
	List<School> findByOrderByNameAsc();
}
