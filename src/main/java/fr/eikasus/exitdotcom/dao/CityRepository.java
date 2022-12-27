package fr.eikasus.exitdotcom.dao;

import fr.eikasus.exitdotcom.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository public interface CityRepository extends JpaRepository<City, Long>
{
}
