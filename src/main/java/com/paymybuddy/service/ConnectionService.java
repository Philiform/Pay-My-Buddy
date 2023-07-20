package com.paymybuddy.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.enumerations.ResponseForOperation;
import com.paymybuddy.model.User;
import com.paymybuddy.model.UserConnection;
import com.paymybuddy.repository.UserConnectionRepository;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.security.UserConnected;
import com.paymybuddy.service.dto.EmailUserConnectionDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class ConnectionService {

	@Autowired
	private UserConnectionRepository userConnectionRepository;

	@Autowired
	private UserRepository userRepository;

	public Page<EmailUserConnectionDTO> getListEmailAndPseudoByUserSender(final int userSender, final Pageable pageable) {
		List<EmailUserConnectionDTO> listDTO = new ArrayList<>();

		try {
			// chercher tous les UserConnections avec l'id de userSender
			List<UserConnection> listUserConnection = userConnectionRepository.findByUserSenderNotDeleted(UserConnected.getId());

			// chercher les emails pour chaque UserConnection
			for(UserConnection userConnection: listUserConnection) {
				User user = userRepository.findById(userConnection.getUserRecipient().getUserId()).get();

				EmailUserConnectionDTO t = new EmailUserConnectionDTO(
						user.getEmail(),
						userConnection);

				listDTO.add(t);
			}
			// trier les données par "email"
			Comparator<EmailUserConnectionDTO> dtoComparator = Comparator.comparing(EmailUserConnectionDTO::getEmail);
			listDTO.sort(dtoComparator);

		} catch (Exception e) {
			log.error(e.toString());
		}

		return new PageImpl<EmailUserConnectionDTO>(listDTO);
	}

	public ResponseForOperation saveConnection(EmailUserConnectionDTO emailUserConnectionDTO, int userId) {
		try {
			// récupérer l'userId qui correspond à l'email
			Optional<User> userRecipient = userRepository.findByEmail(emailUserConnectionDTO.getEmail());
			String pseudo = emailUserConnectionDTO.getUserConnection().getPseudo();

			log.debug("emailUserConnectionDTO = " + emailUserConnectionDTO);

			// vérifier que l'email appartient à une personne pouvant recevoir de l'argent
			if(userRecipient.isEmpty() || !userRecipient.get().getRoles().get(0).getRole().equals("USER")) {
				log.debug(emailUserConnectionDTO.getEmail() + " IS NOT VALID");
				return ResponseForOperation.EMAIL_INVALID;
			}

			// vérifier que le pseudo n'est pas vide
			if(pseudo.equals("")) {
				log.debug("PSEUDO IS EMPTY");
				return ResponseForOperation.PSEUDO_IS_EMPTY;
			}

			// vérifier si l'UserConnection existe
			User userSender = userRepository.getReferenceById(userId);

			log.debug("userSender.getUserConnections = " + userSender.getUserConnections());

			UserConnection userConnection = null;
			Optional<User> userRecipientPresent = Optional.empty();
			boolean pseudoIsPresent = false;
			Optional<User> userRecipientDeletedPresent = Optional.empty();
			boolean pseudoDeletedIsPresent = false;

			for(UserConnection uc: userSender.getUserConnections()) {
				if(!uc.isDeleted()) {
					if(uc.getUserRecipient().equals(userRecipient.get())) {
						userRecipientPresent = userRecipient;
						userConnection = uc;
					}

					if(uc.getPseudo().equals(pseudo)) {
						pseudoIsPresent = true;
					}
				} else {
					if(uc.getUserRecipient().equals(userRecipient.get())) {
						userRecipientDeletedPresent = userRecipient;
						userConnection = uc;
					}

					if(uc.getPseudo().equals(pseudo)) {
						pseudoDeletedIsPresent = true;
					}
				}
			}

			log.debug("userRecipientPresent = " + userRecipientPresent);
			log.debug("pseudoIsPresent = " + pseudoIsPresent);
			log.debug("userRecipientDeletedPresent = " + userRecipientDeletedPresent);
			log.debug("pseudoDeletedIsPresent = " + pseudoDeletedIsPresent);

			// SI userRecipientPresent n'est pas vide ET QUE pseudoPresent est faux
			// ALORS renommer le pseudo
			if(!userRecipientPresent.isEmpty() && !pseudoIsPresent) {
				log.debug("case 1");

				userConnection.setPseudo(pseudo);

				log.debug("userConnection = " + userConnection);

				userConnectionRepository.save(userConnection);
			}

			// SI userRecipientDeletedPresent n'est pas vide
			// ALORS reactiver la connexion et modifier le pseudo
			else if(!userRecipientDeletedPresent.isEmpty()) {
				log.debug("case 2");

				userConnection.setDeleted(false);
				userConnection.setPseudo(pseudo);

				log.debug("userConnection = " + userConnection);

				userConnectionRepository.save(userConnection);
			}

			// SI userRecipientPresent est vide ET QUE pseudoPresent est false
			// ALORS renommer le pseudo
			else if(userRecipientPresent.isEmpty() && !pseudoIsPresent) {
				log.debug("case 3");

				userConnection = new UserConnection();

				userConnection.setUserSender(userSender);
				userConnection.setUserRecipient(userRecipient.get());
				userConnection.setPseudo(emailUserConnectionDTO.getUserConnection().getPseudo());
				userConnection.setDeleted(false);

				userSender.getUserConnections().add(userConnection);

				log.debug("userConnection = " + userConnection);

				userConnectionRepository.save(userConnection);
			}

		} catch (Exception e) {
			log.error(e.toString());
			return ResponseForOperation.ERROR;
		}

		return ResponseForOperation.OK;
	}

	public void deleteConnection(int userConnectionId) {
		try {
			if(userConnectionId > 0) {
				Optional<UserConnection> userConnection = userConnectionRepository.findById(userConnectionId);

				if(!userConnection.isEmpty()) {
					userConnection.get().setDeleted(true);
					userConnectionRepository.save(userConnection.get());
				}
			}
		} catch (Exception e) {
			log.error(e.toString());
		}
	}
}
