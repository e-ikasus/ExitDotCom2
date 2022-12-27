package fr.eikasus.exitdotcom.entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity @Table(name = "locations")
@Getter @Setter @NoArgsConstructor
public class Location
{
	/* ******************************* */
	/* Constants defined for the class */
	/* ******************************* */

	public static final int NAME_MIN_LENGTH = 3;
	public static final int NAME_MAX_LENGTH = 64;

	public static final int STREET_MIN_LENGTH = 8;
	public static final int STREET_MAX_LENGTH = 128;

	/* ************* */
	/* Class members */
	/* ************* */

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Expose
	private Long identifier;

	@Column(length = NAME_MAX_LENGTH, nullable = false, unique = true)
	@Expose
	private String name;

	@Column(length = STREET_MAX_LENGTH, nullable = false)
	@Expose
	private String street;

	@Expose
	private float latitude;

	@Expose
	private float longitude;

	@ManyToOne
	@JoinColumn(name="city")
	@Expose
	private City city;

	@OneToMany(mappedBy = "location")
	private Set<Exit> exits = new HashSet<>();

	/* *************** */
	/* Getters/setters */
	/* *************** */

	public Set<Exit> getExits()
	{
		return new HashSet<>(exits);
	}

	/* ************ */
	/* Constructors */
	/* ************ */

	public Location(String name, String street, float latitude, float longitude, City city)
	{
		this.name = name;
		this.street = street;
		this.latitude = latitude;
		this.longitude = longitude;

		if (city != null) city.addLocation(this);
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	public Location addExit(Exit exit)
	{
		if ((exit != null) && (!exits.contains(exit)))
		{
			exits.add(exit);

			exit.setLocation(this);
		}

		return this;
	}

	public Location removeExit(Exit exit)
	{
		if ((exit != null) && (exits.contains(exit)))
		{
			exits.remove(exit);

			exit.setLocation(null);
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
		Location location = (Location) o;
		return Float.compare(location.latitude, latitude) == 0 && Float.compare(location.longitude, longitude) == 0 && Objects.equals(identifier, location.identifier) && name.equals(location.name) && street.equals(location.street);
	}

	@Override public int hashCode()
	{
		return Objects.hash(identifier, name, street, latitude, longitude);
	}
}
