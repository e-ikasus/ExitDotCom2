package fr.eikasus.exitdotcom.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity public class SecurityConfig
{
	@Bean public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http
			.authorizeRequests()
			.antMatchers("/css/**", "/images/**", "/js/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin().loginPage("/security/login").permitAll();

		return http.build();
	}

	@Bean public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
}