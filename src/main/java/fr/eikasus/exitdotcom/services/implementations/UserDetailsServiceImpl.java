package fr.eikasus.exitdotcom.services.implementations;

import fr.eikasus.exitdotcom.dao.StudentRepository;
import fr.eikasus.exitdotcom.entities.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service public class UserDetailsServiceImpl implements UserDetailsService
{
	private final StudentRepository studentRepository;

	@Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		Student student = studentRepository.findByUsername(username);

		if (student == null) throw new UsernameNotFoundException(String.format("User %s not found.", username));

		return new UserDetailsImpl(student);
	}
}
