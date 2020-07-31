package com.personal.page.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_account")
public class UserAccount implements Serializable {

	private static final long serialVersionUID = 1006867765376901113L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotNull(message = "first name cannot be null")
	@Size(min = 1, max = 15)
	@Column(name = "first_name")
	private String firstName;

	@NotNull(message = "last name cannot be null")
	@Size(min = 1, max = 15)
	@Column(name = "last_name")
	private String lastName;

//	@Email(message = "Email should be valid")
	@Column(name = "email")
	private String email;

	@NotNull(message = "date of birth cannot be null")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@JsonFormat(pattern = "MM/dd/yyyy")
	private LocalDate dateOfBirth;
	@Column(name = "password")
	private String password;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Collection<Role> roles;

	@Column(name = "confirmation_token")
	private String confirmationToken;

	@Column(name = "enabled")
	private boolean enabled;

	@Column(name = "username")
	private String username;
	@Column(name = "active")
	private boolean active;

}
