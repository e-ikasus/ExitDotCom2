package fr.eikasus.exitdotcom;

import fr.eikasus.exitdotcom.dao.StudentRepository;
import fr.eikasus.exitdotcom.misc.PopulateDataBase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication public class ExitDotComApplication implements CommandLineRunner
{
	private final PopulateDataBase populateDataBase;
	private final StudentRepository studentRepository;

	public static void main(String[] args)
	{
		SpringApplication.run(ExitDotComApplication.class, args);
	}

	@Override public void run(String... args)
	{
		if (studentRepository.findByUsername("admin") == null)
			populateDataBase.populate();
	}
}
