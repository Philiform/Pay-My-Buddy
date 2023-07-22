package com.paymybuddy.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.model.BankAccount;
import com.paymybuddy.model.CreditCard;
import com.paymybuddy.model.ExternalBankAccount;
import com.paymybuddy.model.InternalBankAccount;
import com.paymybuddy.model.User;
import com.paymybuddy.model.UserConnection;
import com.paymybuddy.repository.BankAccountRepository;
import com.paymybuddy.repository.CreditCardRepository;
import com.paymybuddy.repository.ExternalBankAccountRepository;
import com.paymybuddy.repository.InternalBankAccountRepository;
import com.paymybuddy.repository.UserConnectionRepository;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.security.UserConnected;
import com.paymybuddy.service.dto.ProfileDTO;

import lombok.extern.slf4j.Slf4j;

// TODO: Auto-generated Javadoc
/** The Constant log. */
@Slf4j
@Transactional
@Service
public class ProfileService {

	/** The user repository. */
	@Autowired
	private UserRepository userRepository;

	/** The user connection repository. */
	@Autowired
	private UserConnectionRepository userConnectionRepository;

	/** The internal bank account repository. */
	@Autowired
	private InternalBankAccountRepository internalBankAccountRepository;

	/** The external bank account repository. */
	@Autowired
	private ExternalBankAccountRepository externalBankAccountRepository;

	/** The bank account repository. */
	@Autowired
	private BankAccountRepository bankAccountRepository;

	/** The credit card repository. */
	@Autowired
	private CreditCardRepository creditCardRepository;

	/**
	 * Gets the profile by user id.
	 *
	 * @param userId the user id
	 * @return the profile by user id
	 */
	public Optional<ProfileDTO> getProfileByUserId(final int userId) {
		ProfileDTO profileDTO = new ProfileDTO();
		List<String> profile = new ArrayList<>();
		String profileString = "";

		try {
			profileString = userRepository.findProfileByUserId(userId);

			log.debug("userRepository.findProfileByUserId: " + profileString);

			if(profileString.equals("")) {
				return Optional.empty();
			}

			profile = Arrays.asList(profileString.split(","));

			log.debug("profile: " + profile);

			profileDTO.setEmail(profile.get(0));
			profileDTO.setFirstName(profile.get(1));
			profileDTO.setLastName(profile.get(2));

			log.debug("profileDTO: " + profileDTO.toString());
		} catch (Exception e) {
			log.error("profileDTO: NOT FIND by user: " + userId);
			log.error(e.toString());

			return Optional.empty();
		}

		return Optional.of(profileDTO);
	}

	/**
	 * Save profile.
	 *
	 * @param profileDTO the profile DTO
	 */
	public void saveProfile(ProfileDTO profileDTO) {
		int userId = UserConnected.getId();

		try {
			userRepository.saveProfileByUserId(profileDTO.getEmail(), profileDTO.getFirstName(), profileDTO.getLastName(), userId);

			log.debug("PROFILE SAVE : user.id = " + String.valueOf(userId));
		} catch (Exception e) {
			log.error("PROFILE NOT SAVE : user.id = " + String.valueOf(userId));
			log.error(e.toString());
		}
	}

	/**
	 * Gets the external bank account by user id.
	 *
	 * @param userId the user id
	 * @return the external bank account by user id
	 */
	public Optional<ExternalBankAccount> getExternalBankAccountByUserId(int userId) {
		try {
			return externalBankAccountRepository.findByUserId(userId);
		} catch (Exception e) {
			log.error(e.toString());
		}

		return Optional.empty();
	}

	/**
	 * Save bank account.
	 *
	 * @param externalBankAccount the external bank account
	 * @return the optional
	 */
	public Optional<BankAccount> saveBankAccount(ExternalBankAccount externalBankAccount) {
		Optional<BankAccount> bankAccountSaved = Optional.empty();

		try {
			log.debug("externalBankAccount = " + externalBankAccount);

			if(externalBankAccount.getBankAccount().getBankAccountId() == 0) {
				BankAccount bankAccount = new BankAccount();
				bankAccount.setNumber(externalBankAccount.getBankAccount().getNumber());
				bankAccount.setExternalBankAccount(null);
				bankAccount.setDeleted(false);

				bankAccountRepository.save(bankAccount);

				log.debug("bankAccount = " + bankAccount);

				ExternalBankAccount externalBankAccount2 = externalBankAccountRepository.findById(externalBankAccount.getExternalBankAccountId()).get();
				externalBankAccount2.setBankAccount(bankAccount);

				log.debug("externalBankAccount2 = " + externalBankAccount2);

				externalBankAccountRepository.save(externalBankAccount2);

				bankAccount.setExternalBankAccount(externalBankAccount2);

				bankAccountSaved = Optional.of(bankAccountRepository.save(bankAccount));

				log.debug("bankAccount = " + bankAccount);
				log.debug("BANK ACCOUNT SAVE");

			}/* else {
				log.debug("BANK ACCOUNT SAVE : bankAccount is modified");
			}*/

		} catch (Exception e) {
			log.error(e.toString());

			return Optional.empty();
		}

		return bankAccountSaved;
	}

	/**
	 * Save credit card.
	 *
	 * @param externalBankAccount the external bank account
	 * @return the optional
	 */
	public Optional<CreditCard> saveCreditCard(ExternalBankAccount externalBankAccount) {
		Optional<CreditCard> creditCardSaved = Optional.empty();

		try {
			log.debug("externalBankAccount = " + externalBankAccount);

			if(externalBankAccount.getCreditCard().getCreditCardId() == 0) {
				CreditCard creditCard = new CreditCard();
				creditCard.setNumber(externalBankAccount.getCreditCard().getNumber());
				creditCard.setExpiresEndMonth(externalBankAccount.getCreditCard().getExpiresEndMonth());
				creditCard.setExpiresEndYear(externalBankAccount.getCreditCard().getExpiresEndYear());
				creditCard.setValidationValue(externalBankAccount.getCreditCard().getValidationValue());
				creditCard.setExternalBankAccount(null);
				creditCard.setDeleted(false);

				creditCardRepository.save(creditCard);

				log.debug("creditCard = " + creditCard);

				ExternalBankAccount externalBankAccount2 = externalBankAccountRepository.findById(externalBankAccount.getExternalBankAccountId()).get();
				externalBankAccount2.setCreditCard(creditCard);

				log.debug("externalBankAccount2 = " + externalBankAccount2);

				externalBankAccountRepository.save(externalBankAccount2);

				creditCard.setExternalBankAccount(externalBankAccount2);

				creditCardSaved = Optional.of(creditCardRepository.save(creditCard));

				log.debug("creditCard = " + creditCard);
				log.debug("CREDIT CARD SAVE");

			}/* else {
				log.debug("CREDIT CARD NOT SAVE : creditCard is empty");
			}*/
		} catch (Exception e) {
			log.error(e.toString());

			return Optional.empty();
		}

		return creditCardSaved;
	}

	/**
	 * Delete profile.
	 */
	public void deleteProfile() {
		try {
			int userId = UserConnected.getId();
			User user = UserConnected.getUserConnected();

			// supprimer UserConnection SENDER et RECIPIENT
			List<UserConnection> listUserConnection = userConnectionRepository.findToDelete(userId);
			for(UserConnection uc: listUserConnection) {
				uc.setDeleted(true);
				userConnectionRepository.save(uc);
			}
			// supprimer BankAccount
			deleteBankAccount(user.getExternalBankAccount().getExternalBankAccountId());

			// supprimer CreditCard
			deleteCreditCard(user.getExternalBankAccount().getExternalBankAccountId());

			// supprimer ExternalBankAccount
			ExternalBankAccount externalBankAccount = externalBankAccountRepository.getReferenceById(user.getExternalBankAccount().getExternalBankAccountId());
			externalBankAccount.setDeleted(true);
			externalBankAccountRepository.save(externalBankAccount);

			// supprimer InternalBankAccount
			InternalBankAccount internalBankAccount = internalBankAccountRepository.getReferenceById(user.getInternalBankAccount().getInternalBankAccountId());
			internalBankAccount.setDeleted(true);
			internalBankAccountRepository.save(internalBankAccount);

			// supprimer Role
			user.removeRole(user.getRoles().get(0));

			// supprimer User
			user.setDeleted(true);
			userRepository.save(user);


		} catch (Exception e) {
			log.error(e.toString());
		}
	}

	/**
	 * Delete bank account.
	 *
	 * @param externalBankAccountId the external bank account id
	 */
	public void deleteBankAccount(int externalBankAccountId) {
		try {
			if(externalBankAccountId > 0) {
				ExternalBankAccount externalBankAccount = externalBankAccountRepository.findById(externalBankAccountId).get();
				BankAccount bankAccount = externalBankAccount.getBankAccount();

				if(!Optional.of(bankAccount).isEmpty()) {
					log.debug("BANK ACCOUNT : bankAccount is not null");

					bankAccount.setDeleted(true);
					bankAccountRepository.save(bankAccount);

					externalBankAccount.setBankAccount(null);
					externalBankAccountRepository.save(externalBankAccount);
				}

				log.debug("BANK ACCOUNT DELETE");

			}

		} catch (Exception e) {
			log.error(e.toString());
		}
	}

	/**
	 * Delete credit card.
	 *
	 * @param externalBankAccountId the external bank account id
	 */
	public void deleteCreditCard(int externalBankAccountId) {
		try {
			if(externalBankAccountId > 0) {
				ExternalBankAccount externalBankAccount = externalBankAccountRepository.findById(externalBankAccountId).get();

//				log.debug("CREDIT CARD : externalBankAccount = " + externalBankAccount);

				CreditCard creditCard = externalBankAccount.getCreditCard();

//				log.debug("CREDIT CARD : creditCard = " + creditCard);

				if(!Optional.of(creditCard).isEmpty()) {
					log.debug("CREDIT CARD : creditCard is not null");

					creditCard.setDeleted(true);
					creditCardRepository.save(creditCard);

					externalBankAccount.setCreditCard(null);
					externalBankAccountRepository.save(externalBankAccount);
				}

				log.debug("CREDIT CARD DELETE");

			}

		} catch (Exception e) {
			log.error(e.toString());
		}
	}
}
