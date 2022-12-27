package fr.eikasus.exitdotcom.misc;

import fr.eikasus.exitdotcom.entities.School;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Search criteria for exits.
 * <p></p>
 * This class contains all the search criteria accessible to the student to
 * filter the exits list he wants to be displayed. It is instantiated each time
 * the student click on the search button in the HTML form. Because the
 * framework always instantiate the class even if no form is return by the
 * client with all properties null, the only way to determine if this instance
 * is really sent is by the use of the valid property which is always true when
 * the form is really sent.
 */

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SearchCriteria
{
	// School attached to the exit.
	private School school;

	// Keyword that an exit must have in his name.
	private String content;

	// Start date of the exit.
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date start;

	// End date of the exit.
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date end;

	// Is the current student the organiser of the exit.
	private boolean organiser;

	// Is the current student subscribed to the exit.
	private boolean subscribe;

	// Is the current student unsubscribed to the exit.
	private boolean unsubscribe;

	// Is the exit in the past.
	private boolean past;

	// Is the instance valid.
	private boolean valid;
}
