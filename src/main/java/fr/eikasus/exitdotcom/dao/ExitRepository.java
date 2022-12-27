package fr.eikasus.exitdotcom.dao;

import fr.eikasus.exitdotcom.entities.Exit;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface used to handle exits persistence.
 * <p></p>
 * This interface define all the methods required to handle exits persistence
 * context and that are supplied by the framework. For additional methods, see
 * {@link ExitRepositoryCustom ExitRepositoryCustom} interface for more details.
 */

public interface ExitRepository extends JpaRepository<Exit, Long>, ExitRepositoryCustom
{
}
