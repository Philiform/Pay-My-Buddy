package com.paymybuddy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.enumerations.ResponseForOperation;
import com.paymybuddy.model.ExternalBankAccount;
import com.paymybuddy.model.InternalBankAccount;
import com.paymybuddy.model.Role;
import com.paymybuddy.model.User;
import com.paymybuddy.model.UserConnection;
import com.paymybuddy.repository.ExternalBankAccountRepository;
import com.paymybuddy.repository.InternalBankAccountRepository;
import com.paymybuddy.repository.RoleRepository;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.service.dto.NewUserDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class CreateUserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ExternalBankAccountRepository externalBankAccountRepository;

	@Autowired
	private InternalBankAccountRepository internalBankAccountRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public ResponseForOperation saveNewUser(final NewUserDTO newUserDTO) {
		try {
			if(!userRepository.findByEmailNotDeleted(newUserDTO.getEmail()).isEmpty()) {
				return ResponseForOperation.EMAIL_MUST_BE_CHANGED;
			}

			if(!newUserDTO.getPassword().equals(newUserDTO.getMatchingPassword()) ) {
				return ResponseForOperation.ERROR_CONFIRM_PASSWORD;
			}
			User user = new User();

			user.setFirstName(newUserDTO.getFirstName());
			user.setLastName(newUserDTO.getLastName());
			user.setEmail(newUserDTO.getEmail());
			user.setPassword(passwordEncoder.encode(newUserDTO.getPassword()));

			userRepository.save(user);

			// CREER un ExternalBankAccount
			ExternalBankAccount externalBankAccount = new ExternalBankAccount();
			externalBankAccount.setUser(user);

			externalBankAccountRepository.save(externalBankAccount);

			user.setExternalBankAccount(externalBankAccount);

			userRepository.save(user);

			// CREER un InternalBankAccount
			InternalBankAccount internalBankAccount = new InternalBankAccount();
			internalBankAccount.setUser(user);

			internalBankAccountRepository.save(internalBankAccount);

			user.setInternalBankAccount(internalBankAccount);

			userRepository.save(user);

			// CREER un UserConnection
			UserConnection userConnection = new UserConnection();
			userConnection.setUserRecipient(user);
			userConnection.setPseudo("Me");

			user.addConnection(userConnection);

			// CREER un Role
			Role role = roleRepository.getReferenceById(3);
			role.setRole("USER");

			user.addRole(role);

		} catch (Exception e) {
			log.error(e.toString());
			return ResponseForOperation.ERROR;
		}

		return ResponseForOperation.OK;
	}
}
