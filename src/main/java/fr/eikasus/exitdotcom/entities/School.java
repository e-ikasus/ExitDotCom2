package fr.eikasus.exitdotcom.entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity @Table(name="School")
@Getter @Setter @NoArgsConstructor
public class School
{
	/* ******************************* */
	/* Constants defined for the class */
	/* ******************************* */

	private static final int NAME_MIN_LENGTH = 3;
	private static final int NAME_MAX_LENGTH = 64;

	/* ************* */
	/* Class members */
	/* ************* */

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Expose
	private Long identifier;

	@Column(length = NAME_MAX_LENGTH, nullable = false, unique = true)
	@Expose
	private String name;

	@OneToMany(mappedBy = "school")
	private Set<Exit> exits = new HashSet<>();

	@OneToMany(mappedBy = "school")
	private Set<Student> students = new HashSet<>();

	/* *************** */
	/* Getters/setters */
	/* *************** */

	public Set<Exit> getExits()
	{
		return new HashSet<>(exits);
	}

	public Set<Student> getStudents()
	{
		return new HashSet<>(students);
	}

	/* ************ */
	/* Constructors */
	/* ************ */

	public School(String name)
	{
		this.name = name;
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	public School addExit(Exit exit)
	{
		if ((exit != null) && (!exits.contains(exit)))
		{
			exits.add(exit);

			exit.setSchool(this);
		}

		return this;
	}

	public School removeExit(Exit exit)
	{
		if ((exit != null) && (exits.contains(exit)))
		{
			exits.remove(exit);

			exit.setSchool(null);
		}

		return this;
	}

	public School addStudent(Student student)
	{
		if ((student != null) && (!students.contains(student)))
		{
			students.add(student);

			student.setSchool(this);
		}

		return this;
	}

	public School removeStudent(Student student)
	{
		if ((student != null) && (students.contains(student)))
		{
			students.remove(student);

			student.setSchool(null);
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
		School school = (School) o;
		return Objects.equals(identifier, school.identifier) && name.equals(school.name);
	}

	@Override public int hashCode()
	{
		return Objects.hash(identifier, name);
	}
}
