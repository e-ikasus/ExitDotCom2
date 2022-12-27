package fr.eikasus.exitdotcom.services.implementations;

import fr.eikasus.exitdotcom.entities.Role;
import fr.eikasus.exitdotcom.entities.Student;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@Getter
public class UserDetailsImpl implements UserDetails
{
	private final Long identifier;

	private final String username;

	private final String password;

	private final boolean isActive;

	private final Collection<Role> roles;

	/* ************ */
	/* Constructors */
	/* ************ */

	public UserDetailsImpl(@NotNull Student student)
	{
		identifier = student.getIdentifier();
		username = student.getUsername();
		password = student.getPassword();
		roles = student.getRoles();
		isActive = student.isActive();
	}

	/* *************************************** */
	/* Methods implemented for spring security */
	/* *************************************** */

	public Collection<GrantedAuthority> getAuthorities()
	{
		String rolesString = roles.stream().map(role -> "ROLE_" + role.getName()).collect(Collectors.joining(","));

		return AuthorityUtils.commaSeparatedStringToAuthorityList(rolesString);
	}

	public boolean isAccountNonExpired()
	{
		return true;
	}

	public boolean isAccountNonLocked()
	{
		return true;
	}

	public boolean isCredentialsNonExpired()
	{
		return true;
	}

	public boolean isEnabled()
	{
		return isActive;
	}
}
