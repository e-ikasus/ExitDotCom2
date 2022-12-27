package fr.eikasus.exitdotcom.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "states")
@Getter @Setter @NoArgsConstructor
public class State
{
	/* ******************************* */
	/* Constants defined for the class */
	/* ******************************* */

	public static final int NAME_MIN_LENGTH = 3;
	public static final int NAME_MAX_LENGTH = 64;

	/* ************* */
	/* Class members */
	/* ************* */

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long identifier;

	@Column(length = 2, nullable = false, unique = true)
	private String code;

	@Column(length = NAME_MAX_LENGTH, nullable = false, unique = true)
	private String name;

	@OneToMany(mappedBy = "state")
	private List<Exit> exits = new ArrayList<>();

	/* ************ */
	/* Constructors */
	/* ************ */

	public State(@NotNull StateCode code)
	{
		this.code = code.name();

		this.name = code.getValue();
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	public State addExit(Exit exit)
	{
		if ((exit != null) && (!exits.contains(exit)))
		{
			exits.add(exit);

			exit.setState(this);
		}

		return this;
	}

	public State removeExit(Exit exit)
	{
		if ((exit != null) && (exits.contains(exit)))
		{
			exits.remove(exit);

			exit.setState(null);
		}

		return this;
	}
}
