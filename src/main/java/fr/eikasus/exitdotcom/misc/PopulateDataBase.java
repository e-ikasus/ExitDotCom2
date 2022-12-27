package fr.eikasus.exitdotcom.misc;

import com.github.javafaker.Faker;
import fr.eikasus.exitdotcom.dao.*;
import fr.eikasus.exitdotcom.entities.*;
import fr.eikasus.exitdotcom.services.interfaces.Tracer;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Populate the database.
 * <p>
 * This class is used to fill the database with consistent data for debugging
 * purpose and developement.
 *
 * @see #populate()
 */

@RequiredArgsConstructor
@Component public class PopulateDataBase
{
	/* ******************************* */
	/* Constants defined for the class */
	/* ******************************* */

	private static final int SCHOOLS_NUMBER = 10;
	private static final int CITIES_NUMBER = 30;
	private static final int LOCATIONS_NUMBER = 10;
	private static final int STUDENTS_NUMBER = 50;
	private static final int EXITS_NUMBER = 70;

	private static final int MAXIMUM_STUDENTS_PER_EXIT = 10;

	private static final String[] closeRange = {"P1D", "P2D", "P3m", "P2m", "P10m"};
	private static final String[] exitRange = {"P1h", "P3h", "P1D", "P2D", "P3M"};

	/* ************* */
	/* Inner Classes */
	/* ************* */

	private interface Generator
	{
		String execute();
	}

	/* ************* */
	/* Class members */
	/* ************* */

	// List of created roles stored in the database.
	private final Map<String, Role> roles;

	// List of created exit states stored in the database.
	private final List<State> states;

	// List of created cities stored in the database.
	private final List<City> cities;

	// List of created locations stored in the database.
	private final List<Location> locations;

	// List of created schools stored in the database.
	private final List<School> schools;

	// List of created students stored in the database.
	private final List<Student> students;

	// List of created exits stored in the database.
	private final List<Exit> exits;

	// Repository used to manage roles.
	private final RoleRepository roleRepository;

	// Repository used to manage exit states.
	private final StateRepository stateRepository;

	// Repository used to manage cities.
	private final CityRepository cityRepository;

	// Repository used to manage locations.
	private final LocationRepository locationRepository;

	// Repository used to manage schools.
	private final SchoolRepository schoolRepository;

	// Repository used to manage students.
	private final StudentRepository studentRepository;

	// Repository used to manage exits.
	private final ExitRepository exitRepository;

	// Encoder used for students password.
	private final PasswordEncoder passwordEncoder;

	// List of generated strings.
	private final Map<String, Boolean> generatedNames;

	// Logger used to output messages.
	private final Tracer tracer;

	// Faker used to generate data.
	private Faker faker;

	// Random object used for random associations.
	private Random randomList;

	/* ******************* */
	/* Methods implemented */
	/* ******************* */

	/**
	 * Fill the database.
	 * <p>
	 * This method is used to fill the database with consistent data. For the
	 * process to fully success, the database must be empty.
	 */

	public void populate()
	{
		faker = new Faker(new Locale("fr-FR"));

		randomList = new Random();

		tracer.info("\nCreating roles ...");
		createRoles();

		tracer.info("\nCreating states ...");
		createStates();

		tracer.info("\nCreating cities ...");
		createCities();

		tracer.info("\nCreating locations ...");
		createLocations();

		tracer.info("\nCreating schools ...");
		createSchools();

		tracer.info("\nCreating students ...");
		createStudents();

		tracer.info("\nCreating exits ...");
		createExits();
	}

	/* ************** */
	/* Helper Methods */
	/* ************** */

	/**
	 * Create roles.
	 * <p>
	 * This method is used to create roles attached to users named students. Each
	 * student can have more than one role.
	 */

	private void createRoles()
	{
		// Create role for admin users.
		roles.put("ADMIN", roleRepository.save(new Role("ADMIN")));

		// Create role for normal users.
		roles.put("USER", roleRepository.save(new Role("USER")));
	}

	/**
	 * Create exits states.
	 * <p>
	 * This method create all the states that can have an exit. Those states are
	 * defined by the StateCode class. An exit can only have one state.
	 */

	private void createStates()
	{
		State state;

		// For every defined state.
		for (StateCode code : StateCode.values())
		{
			// Create a new state with defined code and name.
			state = new State(code);

			// Save the state to the database.
			state = stateRepository.save(state);

			// And add it to the list
			states.add(state);
		}

		// Display created states including identifier and other.
		states.forEach(s -> tracer.info(s.toString()));
	}

	/**
	 * Create the cities.
	 * <p>
	 * This method create the cities that contain locations of exits. The number
	 * of created cities is determined by the CITIES_NUMBER constant. Actually, it
	 * will create {@value #CITIES_NUMBER} cities.
	 */

	private void createCities()
	{
		City city;
		String name;

		for (int i = 0; i < CITIES_NUMBER; i++)
		{
			// Unique name of the city.
			name = randomString((i == 0), () -> faker.address().cityName());

			// Create a new city.
			city = new City(name, faker.address().zipCode());

			// Save the newly created cuty into the database.
			city = cityRepository.save(city);

			// And add it to the list.
			cities.add(city);
		}

		// Display created cities including identifier and other.
		cities.forEach(c -> tracer.info(c.toString()));
	}

	/**
	 * Create the locations.
	 * <p>
	 * This method create the locations where exits are organised. The number of
	 * created locations is determined by the LOCATIONS_NUMBER constant. Actually,
	 * it will create {@value #LOCATIONS_NUMBER} locations. Because locations are
	 * in cities, they must be created after cities are. Created locations are
	 * attached to cites randomly.
	 */

	private void createLocations()
	{
		City city;
		Location location;
		String name, street;
		float latitude, longitude;

		for (int i = 0; i < LOCATIONS_NUMBER; i++)
		{
			// Select a randomly city.
			city = (City) randomObject(cities);

			name = randomString((i == 0), () -> faker.funnyName().name());
			street = faker.address().streetName();
			latitude = Float.parseFloat(faker.address().latitude().replace(',', '.'));
			longitude = Float.parseFloat(faker.address().longitude().replace(',', '.'));

			// Create a new location attached to a city.
			location = new Location(name, street, latitude, longitude, city);

			// Save the location into the database.
			location = locationRepository.save(location);

			// And add it to the list.
			locations.add(location);
		}

		// Display created locations including identifier and other.
		locations.forEach(l -> tracer.info(l.toString()));
	}

	/**
	 * Create the schools.
	 * <p>
	 * This method create the schools. The number of created schools is determined
	 * by the SCHOOLS_NUMBER constant. Actually, this method will create
	 * {@value #SCHOOLS_NUMBER} schools.
	 */

	private void createSchools()
	{
		School school;
		String name;

		for (int i = 0; i < SCHOOLS_NUMBER; i++)
		{
			// Find a name for the school.
			name = randomString((i == 0), () -> faker.university().name());

			// Crate the school.
			school = new School(name);

			// Save the newly craeted school into the database.
			school = schoolRepository.save(school);

			// And add-it to the list.
			schools.add(school);
		}

		// Display created schools including identifier and other.
		schools.forEach(s -> tracer.info(s.toString()));
	}

	/**
	 * Create the students.
	 * <p>
	 * This method create the students that will organise or register to exits.
	 * The number of created students is determined by the STUDENTS_NUMBER
	 * constant. Actually, this method will create {@value #STUDENTS_NUMBER}
	 * students. Because students have role and are attached to schools, those
	 * both entities must be created before the students are.
	 */

	private void createStudents()
	{
		Student student;
		School school;
		String username, password, firstName, lastName, phoneNumber, email;

		// Password commun to all students.
		password = passwordEncoder.encode("1234");

		// Initialise names list.
		randomString(true, null);

		for (int i = 0; i < STUDENTS_NUMBER; i++)
		{
			username = (i == 0) ? ("admin") : (randomString(false, () -> faker.name().username()));

			firstName = faker.name().firstName();
			lastName = faker.name().lastName();
			phoneNumber = faker.phoneNumber().phoneNumber();
			email = faker.internet().emailAddress();

			// Create a new student.
			student = new Student(username, password, firstName, lastName, phoneNumber, email, (i == 0), true);

			// All students are simple user at least.
			student.addRole(roles.get("USER"));

			// But only the first is an admin.
			if (i == 0) student.addRole(roles.get("ADMIN"));

			// Find a random school.
			school = (School) randomObject(schools);

			// And attach the newly created student to this school.
			school.addStudent(student);

			// Save the newly created student into the database.
			student = studentRepository.save(student);

			// And add it to the list.
			students.add(student);
		}

		// Display created students including identifier and other.
		students.forEach(s -> tracer.info(s.toString()));
	}

	/**
	 * Create the exits.
	 * <p>
	 * This method create the exits which are the main goal of this application.
	 * The number of created exits is determined by the EXITS_NUMBER constant.
	 * Actually, this method will create {@value #EXITS_NUMBER} exits. Exits are
	 * the last entities to create, because they depend on all others. The
	 * MAXIMUM_STUDENTS_PER_EXIT constant determine the number of students that
	 * can be attached to an exit. Currently {@value #MAXIMUM_STUDENTS_PER_EXIT}
	 * will be created at a maximum per exit.
	 */

	@SuppressWarnings({"OptionalGetWithoutIsPresent", "StatementWithEmptyBody"})
	private void createExits()
	{
		Map<Long, Student> studentsToUpdate = new HashMap<>();
		Student student, registeredStudent;
		Location location;
		Exit exit;
		String name, description;
		Date startDate, maxRegisterDate;
		int time, maxUsers;
		Random random = new Random();

		// All new exits will be opened.
		State state = states.stream().filter(c -> c.getCode().equals("OP")).findFirst().get();

		for (int i = 0; i < EXITS_NUMBER; i++)
		{
			// Compute exit properties.
			name = randomString((i == 0), () -> faker.animal().name());
			description = faker.shakespeare().romeoAndJulietQuote();
			maxUsers = random.nextInt(MAXIMUM_STUDENTS_PER_EXIT - 1) + 2;
			time = (random.nextInt(9) + 2) * 15;

			// Limit date for a student to register to the exit.
			maxRegisterDate = computeDate(new Date(), closeRange[random.nextInt(closeRange.length)]);
			// Date of the exit.
			startDate = computeDate((Date) maxRegisterDate.clone(), exitRange[random.nextInt(exitRange.length)]);

			// Search for a student that organize this exit.
			student = (Student) randomObject(students);

			// Search for a location that already belong to a city.
			location = (Location) randomObject(locations);

			// Create the exit.
			exit = new Exit(name, state, startDate, time, maxRegisterDate, maxUsers, description, location, student);

			// Save the newly created exit into the database.
			exit = exitRepository.save(exit);

			for (int j = 0; j < maxUsers; j++)
			{
				while (exit.getStudents().contains(registeredStudent = (Student) randomObject(students)));

				registeredStudent.registerToExit(exit);

				studentsToUpdate.put(registeredStudent.getIdentifier(), registeredStudent);
			}

			// And add it to the exits list.
			exits.add(exit);
		}

		studentsToUpdate.forEach((id,s) -> studentRepository.save(s));

		// Display created exits including identifier and other.
		exits.forEach(e -> tracer.info(e.toString()));
	}

	/**
	 * Generate a random string.
	 * <p>
	 * This method will generate a random string using the generator method
	 * supplied in parameter. This method handle a list of previously generated
	 * strings to ensure that each string returned is unique.
	 *
	 * @param init      Used to initialise the internal list.
	 * @param generator Method used to generate string.
	 *
	 * @return The generated string.
	 */

	@SuppressWarnings("StatementWithEmptyBody")
	private String randomString(boolean init, Generator generator)
	{
		String name;

		if (init) generatedNames.clear();

		if (generator == null) return null;

		while (generatedNames.get(name = generator.execute()) != null) ;

		generatedNames.put(name, true);

		return name;
	}

	/**
	 * Pick a random object in a list.
	 * <p>
	 * This method return an object from the supplied list randomly.
	 *
	 * @param list List to deal with.
	 *
	 * @return Object found.
	 */

	private Object randomObject(@NotNull List<?> list)
	{
		return list.get(randomList.nextInt(list.size()));
	}

	/**
	 * Compute a date.
	 * <p>
	 * This method compute a date from the one supplied in parameter to produce
	 * another date. The way this date is formed is determined by the action
	 * parameter which is a string like "SnU" where 'S' is the direction that can
	 * be 'P' for generating a date after the supplied, or 'M' for a date before
	 * the supplied one. 'U' represent the unit and can be 'D' for days, 'M' for
	 * months, "h" for hours and 'm' for minutes. 'n' represent the number of
	 * units to add or subtract to the supplied date. The date parameter can be
	 * null, which will produce a new reference date based on the current.
	 *
	 * @param date   Date used as reference.
	 * @param action Action to perform.
	 *
	 * @return Computed date.
	 */

	private @NotNull Date computeDate(Date date, @NotNull String action)
	{
		Calendar calendar = Calendar.getInstance();

		// Number of units to add or remove.
		int nbr = Integer.parseInt(action.substring(1, action.length() - 1));

		// Remove or add the desired quantity.
		if (action.charAt(0) == 'M') nbr *= -1;

		// Type of unit to deal with.
		char unit = action.charAt(action.length() - 1);

		// If no date is supplied, take the current.
		if (date == null) date = new Date();

		// Initialise the calendar with the appropriate date.
		calendar.setTime(date);

		if (unit == 'M') calendar.add(Calendar.MONTH, nbr);
		else if (unit == 'D') calendar.add(Calendar.DAY_OF_MONTH, nbr);
		else if (unit == 'h') calendar.add(Calendar.HOUR, nbr);
		else if (unit == 'm') calendar.add(Calendar.MINUTE, nbr);

		// Return the new computed date.
		return calendar.getTime();
	}
}
