package com.openclassrooms.paymybuddy.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Collections;

@Entity
@Data
@Table (name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column (name = "first_name")
	@Size(min=2, max=30, message = "Length should be between 2 and 30")
	private String firstName;

	@Column (name = "last_name")
	@Size(min=2, max=30, message = "Length should be between 2 and 30")
	private String lastName;

	@Email(regexp = "^(.+)@(.+)$", message = "Email should be valid")
	private String email;

	@NotEmpty(message = "Password should not be empty")
	private String password;

	@OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	Balance balance;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Role role;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
		return Collections.singleton(authority);
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
