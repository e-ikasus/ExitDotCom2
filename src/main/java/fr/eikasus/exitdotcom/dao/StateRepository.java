package fr.eikasus.exitdotcom.dao;

import fr.eikasus.exitdotcom.entities.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository public interface StateRepository extends JpaRepository<State, Long>
{
}
