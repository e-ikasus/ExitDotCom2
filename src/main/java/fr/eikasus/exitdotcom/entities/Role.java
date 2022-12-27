package fr.eikasus.exitdotcom.entities;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity @Table(name="roles")
@Getter @Setter @NoArgsConstructor
public class Role
{
	/* ******************************* */
	/* Constants defined for the class */
	/* ******************************* */

	private static final int NAME_MAX_LENGTH = 8;

	/* ************* */
	/* Class members */
	/* ************* */

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long identifier;

	@Column(length = NAME_MAX_LENGTH, nullable = false, unique = false)
	private String name;

	@ManyToMany(mappedBy = "roles")
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

	public Role(String name)
	{
		this.name = name;
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	public Role addStudent(Student student)
	{
		students.add(student);

		return this;
	}

	public Role removeStudent(Student student)
	{
		students.remove(student);

		return this;
	}

	@Override public String toString()
	{
		Gson gson = new Gson();

		return gson.toJson(this);
	}

	@Override public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Role role = (Role) o;
		return Objects.equals(identifier, role.identifier) && name.equals(role.name);
	}

	@Override public int hashCode()
	{
		return Objects.hash(identifier, name);
	}
}
