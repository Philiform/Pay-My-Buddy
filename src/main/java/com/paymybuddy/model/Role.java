package com.paymybuddy.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO: Auto-generated Javadoc
/**
 * Hash code.
 *
 * @return the int
 */
@Data

/**
 * Instantiates a new role.
 */
@NoArgsConstructor

/**
 * Instantiates a new role.
 *
 * @param roleId the role id
 * @param role the role
 * @param users the users
 */
@AllArgsConstructor
@Entity
@JsonIgnoreProperties("users")
@Table(name = "role")
public class Role {

	/** The role id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private int roleId;

	/** The role. */
	@Column(name = "role", unique = true)
	@Size(min = 4, max = 20)
	private String role;

	/** The users. */
	@JsonIgnore
	@ManyToMany(
			fetch = FetchType.EAGER,
			mappedBy = "roles",
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			})
	private List<User> users = new ArrayList<>();

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "[ roleId = " + roleId + ", " +
			"role = " + role + ", " +
			"list(User).size = " + users.size() + " ]";
	}
}
