package fr.eikasus.exitdotcom.entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Class defining an exit.
 * <p></p>
 * This class define an exit made by a particular student attached to a school.
 * An exit is organised for a maximum of students defined at creation time. Each
 * exit has a state that depend on the dates properties. For the ManyToOne or
 * ManyToMany relations on particular properties, don't use their setter. Use
 * the one that belongs to the other class.
 *
 * @see #registerStudent(Student)
 * @see #unregisterStudent(Student)
 * @see #toString()
 * @see #equals(Object)
 * @see #hashCode()
 */

@Entity @Table(name = "exits")
@Getter @Setter @NoArgsConstructor
public class Exit
{
	/* ******************************* */
	/* Constants defined for the class */
	/* ******************************* */

	private static final int NAME_MIN_LENGTH = 3;
	private static final int NAME_MAX_LENGTH = 64;

	private static final int DESC_MIN_LENGTH = 3;
	private static final int DESC_MAX_LENGTH = 256;

	/* ************* */
	/* Class members */
	/* ************* */

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Expose
	private Long identifier;

	@Column(length = NAME_MAX_LENGTH, nullable = false, unique = true)
	@Expose
	private String name;

	@Column(nullable = false)
	@Expose
	private Date startDate;

	@Column(nullable = false)
	@Expose
	private int time;

	@Column(nullable = false)
	@Expose
	private Date maxRegisterDate;

	@Column(nullable = false)
	@Expose
	private int maxUsers;

	@Column(length = DESC_MAX_LENGTH, nullable = false)
	@Expose
	private String description;

	@ManyToOne
	@JoinColumn(name = "state")
	private State state;

	@ManyToOne
	@JoinColumn(name = "location")
	private Location location;

	@ManyToOne
	@JoinColumn(name = "school")
	private School school;

	@ManyToOne
	@JoinColumn(name = "organiser")
	private Student organiser;

	@ManyToMany(mappedBy = "registeredExits")
	private Set<Student> students = new HashSet<>();

	/* *************** */
	/* Getters/setters */
	/* *************** */

	public Set<Student> getStudents()
	{
		return new HashSet<>(students);
	}

	/* ************ */
	/* Constructors */
	/* ************ */

	public Exit(String name, State state, Date startDate, int time, Date maxRegisterDate, int maxUsers, String description, Location location, Student organiser)
	{
		this.name = name;
		this.startDate = startDate;
		this.time = time;
		this.maxRegisterDate = maxRegisterDate;
		this.maxUsers = maxUsers;
		this.description = description;

		if (state != null) state.addExit(this);

		if (location != null) location.addExit(this);

		if (organiser != null) organiser.addOrganisedExit(this);

		if (organiser != null) organiser.getSchool().addExit(this);
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	/**
	 * Add a student.
	 * <p></p>
	 * This method register a student to the exit. This method must not be used
	 * directly, but use that from the student class to allow the bidirectional
	 * relation to stay consistent.
	 *
	 * @param student Student to add.
	 *
	 * @return This exit to chain calls.
	 */

	public Exit registerStudent(Student student)
	{
		students.add(student);

		return this;
	}

	/**
	 * Remove a student.
	 * <p></p>
	 * This method unregister a student to the exit. This method must not be used
	 * directly, but use that from the student class to allow the bidirectional
	 * relation to stay consistent.
	 *
	 * @param student Student to remove.
	 *
	 * @return This exit to chain calls.
	 */

	public Exit unregisterStudent(Student student)
	{
		students.remove(student);

		return this;
	}

	/**
	 * Convert the object to a gson string.
	 *
	 * @return Object as a string.
	 */

	@Override public String toString()
	{
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

		return gson.toJson(this);
	}

	/**
	 * Check the equality against one object.
	 * <p></p>
	 * This method check if the supplied object is equal to this. Both objects are
	 * considered equals if they are not null, from the same package and have the
	 * same characterised properties.
	 *
	 * @param o Objet to check with
	 *
	 * @return True if both are equal, false otherwise.
	 */

	@Override public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Exit exit = (Exit) o;
		return time == exit.time && maxUsers == exit.maxUsers && Objects.equals(identifier, exit.identifier) && Objects.equals(name, exit.name) && Objects.equals(startDate, exit.startDate) && Objects.equals(maxRegisterDate, exit.maxRegisterDate) && Objects.equals(description, exit.description) && Objects.equals(state, exit.state) && Objects.equals(location, exit.location) && Objects.equals(school, exit.school) && Objects.equals(organiser, exit.organiser) && Objects.equals(students, exit.students);
	}

	/**
	 * Compute the hash of the object.
	 * <p></p>
	 * This method compute the hash code of the object with his characterised
	 * properties. Note that objects can have the same hash code but be
	 * different.
	 *
	 * @return Hash code of the instance.
	 */

	@Override public int hashCode()
	{
		return Objects.hash(identifier, name, startDate, time, maxRegisterDate, maxUsers, description, state, location, school, organiser, students);
	}
}
