package fr.eikasus.exitdotcom.entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity @Table(name = "cities")
@Getter @Setter @NoArgsConstructor
public class City
{
	/* ******************************* */
	/* Constants defined for the class */
	/* ******************************* */

	public static final int NAME_MIN_LENGTH = 3;
	public static final int NAME_MAX_LENGTH = 64;

	public static final int ZIP_CODE_MIN_LENGTH = 5;
	public static final int ZIP_CODE_MAX_LENGTH = 5;

	/* ************* */
	/* Class members */
	/* ************* */

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Expose
	private Long identifier;

	@Column(length = NAME_MAX_LENGTH, nullable = false)
	@Expose
	private String name;

	@Column(length = ZIP_CODE_MAX_LENGTH, nullable = false, unique = true)
	@Expose
	private String zipCode;

	@OneToMany(mappedBy = "identifier")
	Set<Location> locations = new HashSet<>();

	/* *************** */
	/* Getters/setters */
	/* *************** */

	Set<Location> getLocations()
	{
		return new HashSet<>(locations);
	}

	/* ************ */
	/* Constructors */
	/* ************ */

	public City(String name, String zipCode)
	{
		this.name = name;
		this.zipCode = zipCode;
	}

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	public City addLocation(Location location)
	{
		if ((location != null) && (!locations.contains(location)))
		{
			locations.add(location);

			location.setCity(this);
		}

		return this;
	}

	public City removeLocation(Location location)
	{
		if ((location != null) && (locations.contains(location)))
		{
			locations.remove(location);

			location.setCity(null);
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
		City city = (City) o;
		return Objects.equals(identifier, city.identifier) && name.equals(city.name) && zipCode.equals(city.zipCode);
	}

	@Override public int hashCode()
	{
		return Objects.hash(identifier, name, zipCode);
	}
}
