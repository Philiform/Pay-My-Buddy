package com.paymybuddy.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private int userId;

	@Email
	@Column(name = "email", unique = false)
	@Size(min = 6, max = 50)
	private String email;

	@Column(name = "password", nullable = true, unique = true)
	@Size(min = 50, max = 80)
	private String password;

	@Column(name = "first_name", nullable = true)
	@Size(min = 2, max = 50)
	private String firstName;

	@Column(name = "last_name", nullable = true)
	@Size(min = 2, max = 50)
	private String lastName;

	@OneToOne(
			fetch = FetchType.LAZY,
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			},
			orphanRemoval = true)
	@JoinColumn(name = "internal_bank_account_id", nullable = true)
	private InternalBankAccount internalBankAccount = null;

	@OneToOne(
			fetch = FetchType.LAZY,
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			},
			orphanRemoval = true)
	@JoinColumn(name = "external_bank_account_id", nullable = true)
	private ExternalBankAccount externalBankAccount = null;

	@OneToMany(
			mappedBy = "userSender",
			fetch = FetchType.LAZY,
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			},
			orphanRemoval = true)
	private List<UserConnection> userConnections = new ArrayList<>();

	@ManyToMany(
			fetch = FetchType.EAGER,
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
			}
	)
	@JoinTable(
			name = "user_role",
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id")
	)
	private List<Role> roles = new ArrayList<>();

	@Column(name = "deleted", length = 1, columnDefinition = "boolean default false")
	private boolean deleted;

	public void addConnection(final UserConnection userConnection) {
		userConnections.add(userConnection);
		userConnection.setUserSender(this);
	}

	public void removeConnection(final UserConnection userConnection) {
		userConnections.remove(userConnection);
		userConnection.setUserSender(null);
	}

	public void addRole(final Role role) {
		roles.add(role);
		role.getUsers().add(this);
	}

	public void removeRole(final Role role) {
		roles.remove(role);
		role.setRole(null);
	}

	@Override
	public String toString() {
		String iba = internalBankAccount != null ? String.valueOf(internalBankAccount.getInternalBankAccountId()) : "NULL";
		String eba = externalBankAccount != null ? String.valueOf(externalBankAccount.getExternalBankAccountId()) : "NULL";

		return "[ userId = " + userId + ", " +
			"email = " + email + ", " +
			"firstName = " + firstName + ", " +
			"lastName = " + lastName + ", " +
			"internalBankAccount.internalBankAccountId = " + iba + ", " +
			"externalBankAccount.externalBankAccountId = " + eba + ", " +
//			"list(UserConnection).size = " + userConnections.size() + ", " +
//			"list(Role).size = " + roles.size() + ", " +
			"deleted = " + deleted + " ]";
	}

}
