package fr.eikasus.exitdotcom.entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity @Table(name = "students")
@Getter @Setter @NoArgsConstructor
public class Student
{
	/* ******************************* */
	/* Constants defined for the class */
	/* ******************************* */

	private static final int USERNAME_MIN_LENGTH = 3;
	private static final int USERNAME_MAX_LENGTH = 32;

	private static final int PASSWORD_MIN_LENGTH = 8;
	private static final int PASSWORD_MAX_LENGTH = 32;
	private static final int ENCRYPTED_PASSWORD_MAX_LENGTH = 256;

	private static final int FIRST_NAME_MIN_LENGTH = 3;
	private static final int FIRST_NAME_MAX_LENGTH = 32;

	private static final int LAST_NAME_MIN_LENGTH = 3;
	private static final int LAST_NAME_MAX_LENGTH = 32;

	private static final int PHONE_NUMBER_MIN_LENGTH = 3;
	private static final int PHONE_NUMBER_MAX_LENGTH = 32;

	private static final int EMAIL_MIN_LENGTH = 8;
	private static final int EMAIL_MAX_LENGTH = 32;

	/* ************* */
	/* Class members */
	/* ************* */

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Expose
	private Long identifier;

	@Column(length = USERNAME_MAX_LENGTH, unique = true, nullable = false)
	@Expose
	private String username;

	@Column(length = ENCRYPTED_PASSWORD_MAX_LENGTH, nullable = false)
	@Expose
	private String password;

	@Column(length = FIRST_NAME_MAX_LENGTH, nullable = false)
	@Expose
	private String firstName;

	@Column(length = LAST_NAME_MAX_LENGTH, nullable = false)
	@Expose
	private String lastName;

	@Column(length = PHONE_NUMBER_MAX_LENGTH, nullable = false)
	@Expose
	private String phoneNumber;

	@Column(length = EMAIL_MAX_LENGTH, unique = true, nullable = false)
	@Expose
	private String email;

	@Column(nullable = false)
	@Expose
	private boolean isAdmin;

	@Column(nullable = false)
	@Expose
	private boolean isActive;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="students_roles", joinColumns = @JoinColumn(name = "students"), inverseJoinColumns = @JoinColumn(name = "roles"))
	@Expose
	private Set<Role> roles = new HashSet<>();

	@OneToMany(mappedBy = "organiser")
	private Set<Exit> organisedExits = new HashSet<>();

	@ManyToMany
	@JoinTable(name="students_exits", joinColumns = @JoinColumn(name ="students"), inverseJoinColumns = @JoinColumn(name = "exits"))
	private Set<Exit> registeredExits = new HashSet<>();

	@ManyToOne
	@JoinColumn(name="school")
	private School school;

	/* *************** */
	/* Getters/setters */
	/* *************** */

	public Set<Role> getRoles()
	{
		return new HashSet<>(roles);
	}

	public Set<Exit> getOrganisedExits()
	{
		return new HashSet<>(organisedExits);
	}

	public Set<Exit> getRegisteredExits()
	{
		return new HashSet<>(registeredExits);
	}

	/* ************ */
	/* Constructors */
	/* ************ */

	public Student(String username, String password, String firstName, String lastName, String phoneNumber, String email, boolean isAdmin, boolean isActive)
	{
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.isAdmin = isAdmin;
		this.isActive = isActive;
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	public Student addRole(Role role)
	{
		if ((role != null) && (!roles.contains(role)))
		{
			roles.add(role);

			role.addStudent(this);
		}

		return this;
	}

	public Student removeRole(Role role)
	{
		if ((role != null) && (roles.contains(role)))
		{
			roles.remove(role);

			role.removeStudent(this);
		}

		return this;
	}

	public Student addOrganisedExit(Exit exit)
	{
		if ((exit != null) && (!organisedExits.contains(exit)))
		{
			organisedExits.add(exit);

			exit.setOrganiser(this);
		}

		return this;
	}

	public Student removeOrganisedExit(Exit exit)
	{
		if ((exit != null) && (organisedExits.contains(exit)))
		{
			organisedExits.remove(exit);

			exit.setOrganiser(null);
		}

		return this;
	}

	public Student registerToExit(Exit exit)
	{
		if ((exit != null) && (!registeredExits.contains(exit)))
		{
			registeredExits.add(exit);

			exit.registerStudent(this);
		}

		return this;
	}

	public Student unregisterToExit(Exit exit)
	{
		if ((exit != null) && (registeredExits.contains(exit)))
		{
			registeredExits.remove(exit);

			exit.unregisterStudent(this);
		}

		return this;
	}

	@Override public String toString()
	{
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

		return gson.toJson(this);
	}

	@Override public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Student student = (Student) o;
		return isAdmin == student.isAdmin && isActive == student.isActive && Objects.equals(identifier, student.identifier) && username.equals(student.username) && password.equals(student.password) && firstName.equals(student.firstName) && lastName.equals(student.lastName) && phoneNumber.equals(student.phoneNumber) && email.equals(student.email);
	}

	@Override public int hashCode()
	{
		return Objects.hash(identifier, username, password, firstName, lastName, phoneNumber, email, isAdmin, isActive);
	}
}
