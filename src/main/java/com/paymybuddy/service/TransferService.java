package com.paymybuddy.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.enumerations.ExternalTypeOperation;
import com.paymybuddy.enumerations.InternalTypeOperation;
import com.paymybuddy.enumerations.ResponseForOperation;
import com.paymybuddy.model.ExternalBankAccount;
import com.paymybuddy.model.InternalBankAccount;
import com.paymybuddy.model.MoneyTransfer;
import com.paymybuddy.model.Rate;
import com.paymybuddy.model.UserConnection;
import com.paymybuddy.repository.ExternalBankAccountRepository;
import com.paymybuddy.repository.InternalBankAccountRepository;
import com.paymybuddy.repository.MoneyTransferRepository;
import com.paymybuddy.repository.RateRepository;
import com.paymybuddy.repository.UserConnectionRepository;
import com.paymybuddy.service.dto.TransferMoneyDTO;
import com.paymybuddy.service.dto.TransferTableDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class TransferService {

	@Autowired
	private UserConnectionRepository userConnectionRepository;

	@Autowired
	private MoneyTransferRepository moneyTransferRepository;

	@Autowired
	private RateRepository rateRepository;

	@Autowired
	private InternalBankAccountRepository internalBankAccountRepository;

	@Autowired
	private ExternalBankAccountRepository externalBankAccountRepository;

	public Page<TransferTableDTO> getTransferByUserSender(final int userSender, final Pageable pageable) {
		List<TransferTableDTO> listDTO = new ArrayList<>();

		Page<TransferTableDTO> pageableListDTO = null;
		Page<MoneyTransfer> listMoneyTransfer = null;
		List<UserConnection> listUserConnection = new ArrayList<>();
		List<Integer> listUserConnectionId = new ArrayList<>();
		List<String> listUserConnectionString = new ArrayList<>();

		try {
			// chercher tous les UserConnectionId avec l'id de userSender
			listUserConnectionId = userConnectionRepository.findUserConnectionIdByUserSender(userSender);

			log.debug("\nlistUserConnectionId: " + listUserConnectionId);

			// convertir List<Integer> en List<String>
			listUserConnectionString = listUserConnectionId.stream()
					.map(String::valueOf)
					.collect(Collectors.toList());
			listMoneyTransfer = moneyTransferRepository.findByListUserConnection(listUserConnectionString, pageable);

			log.debug("\nlistMoneyTransfer: " + listMoneyTransfer);

			// créer les DTO
			for(MoneyTransfer m: listMoneyTransfer) {
				TransferTableDTO t = new TransferTableDTO(
						m.getUserConnection().getPseudo(),
						m.getDescription(),
						m.getAmount());

				listDTO.add(t);
			}

			pageableListDTO = new PageImpl<TransferTableDTO>(listDTO, pageable, listMoneyTransfer.getTotalElements());

/*
			log.debug("\npageableListDTO");
			log.debug("\tgetTotalPages: " + pageableListDTO.getTotalPages());
			log.debug("\tgetNumber: " + pageableListDTO.getNumber());
			log.debug("\tgetNumberOfElements: " + pageableListDTO.getNumberOfElements());
			log.debug("\tgetTotalElements: " + pageableListDTO.getTotalElements());
			log.debug("\tgetPageable: " + pageableListDTO.getPageable());
			log.debug("\tgetSort: " + pageableListDTO.getSort());
			log.debug("\tgetContent: " + pageableListDTO.getContent());
			log.debug("listDTO: " + listDTO.toString());
*/
			return pageableListDTO;
		} catch (Exception e) {
			log.error("listMoneyTransfer: " + String.valueOf(listMoneyTransfer));
			log.error("listUserConnection: " + String.valueOf(listUserConnection));
			log.error(e.toString());
		}
		return new PageImpl<TransferTableDTO>(listDTO);
	}

	public TransferMoneyDTO getNewTransferDTO(final int userSender) {
		List<UserConnection> listUserConnection = new ArrayList<>();
		TransferMoneyDTO transferMoneyDTO = new TransferMoneyDTO();

		try {
			Optional<ExternalBankAccount> externalBankAccount = Optional.empty();

			listUserConnection = userConnectionRepository.findByUserSenderAndNotDeletedOrderByPseudo(userSender);
			log.debug("listUserConnection = " + listUserConnection);

			transferMoneyDTO.getListUserConnectionIdPseudoDTO().addAll(listUserConnection);

			externalBankAccount = externalBankAccountRepository.findByUserId(userSender);

			if(!externalBankAccount.isEmpty()) {
				log.debug("externalBankAccount IS NOT EMPTY");

				if(!Objects.isNull(externalBankAccount.get().getBankAccount())) {
					transferMoneyDTO.setBankAccountIsPresent(true);
				}

				if(!Objects.isNull(externalBankAccount.get().getCreditCard())) {
					transferMoneyDTO.setCreditCardIsPresent(true);
				}
			}

			log.debug("transferMoneyDTO = " + transferMoneyDTO.toString());
		} catch (Exception e) {
			log.error(e.toString());
		}

		return transferMoneyDTO;
	}
	public ResponseForOperation saveTransferMoney(TransferMoneyDTO transferMoneyDTO, int userId) {
		try {
//			if(Optional.of(transferMoneyDTO.getUserConnectionId()).isEmpty() || transferMoneyDTO.getAmount() < 1) {
			if(transferMoneyDTO.getUserConnectionId() == 0) {
				log.debug("SAVE TRANSFER MONEY: INTERNAL_ERROR");
				return ResponseForOperation.ERROR;
			}

			UserConnection userConnection = userConnectionRepository.findById(transferMoneyDTO.getUserConnectionId()).get();
			boolean isTransferExterne = false;

			if(userConnection.getUserSender().getUserId() == userConnection.getUserRecipient().getUserId()) {
				isTransferExterne = true;
			}

			log.debug("isTransferExterne = " + isTransferExterne);

			if(isTransferExterne && !transferMoneyDTO.isBankAccountIsPresent() && !transferMoneyDTO.isCreditCardIsPresent()) {
				log.debug("SAVE TRANSFER MONEY: NO_BANK_ACCOUNT_OR_CREDIT_CARD");
				return ResponseForOperation.NO_BANK_ACCOUNT_OR_CREDIT_CARD;
			}

			if(!isTransferExterne && transferMoneyDTO.getAmount() == 0) {
				log.debug("SAVE TRANSFER MONEY: BANK_BALANCE_IS_ZERO");
				return ResponseForOperation.BANK_BALANCE_IS_ZERO;
			}

			// enregistrer les informations du transfert dans la table money_transfer
			float senderBankBalance = userConnection.getUserSender().getInternalBankAccount().getBankBalance();
			Rate rate;
			float amountForSender = 0;
			float commission = 0;

			log.debug("userConnectionId = " + userConnection.getUserConnectionId());
			log.debug("senderAmount = " + senderBankBalance);

			// SI c'est une transaction externe
			if(isTransferExterne) {
				rate = rateRepository.findById(1).get();
				amountForSender = transferMoneyDTO.getAmount();
			} else {
				rate = rateRepository.findLastRate();
				commission = (float) (Math.round(transferMoneyDTO.getAmount() * rate.getRate() * 100.0) / 100.0);
				amountForSender = (float) (Math.round((transferMoneyDTO.getAmount() + commission) * 100.0) / 100.0);
			}

			log.debug("rate = " + rate);
			log.debug("commission = " + commission);

			// vérifier que le montant est suffisant
			if((
					(userConnection.getUserSender().getUserId() != userConnection.getUserRecipient().getUserId())
					||
					(
						(userConnection.getUserSender().getUserId() == userConnection.getUserRecipient().getUserId())
						&&
						!transferMoneyDTO.isTransferFromExternal()
					)
				)
				&& (senderBankBalance - amountForSender) < 0.0) {
				log.debug("SAVE TRANSFER MONEY IMPOSSIBLE: not enough money");
				return ResponseForOperation.NOT_ENOUGH_MONEY;
			}

			float recipientBankBalance = userConnection.getUserRecipient().getInternalBankAccount().getBankBalance();
//			InternalTypeOperation typeOperation = InternalTypeOperation.TRANSFER;
//			String description = transferMoneyDTO.getDescription();

//			log.debug("recipientAmount = " + recipientBankBalance);
//			log.debug("date = " + LocalDateTime.now());
//			log.debug("rate = " + rate);
//			log.debug("typeOperation = " + typeOperation);
//			log.debug("description = " + description);

			MoneyTransfer moneyTransfer = new MoneyTransfer();

			moneyTransfer.setAmount(transferMoneyDTO.getAmount());
			moneyTransfer.setCommission(commission);
			moneyTransfer.setDate(LocalDateTime.now());
			moneyTransfer.setDescription(transferMoneyDTO.getDescription());
			moneyTransfer.setInvoice(null);
			moneyTransfer.setInvoiceSent(false);
			moneyTransfer.setRate(rate);
			moneyTransfer.setUserConnection(userConnection);

			InternalBankAccount sender = null;

			// SI c'est une transaction externe
			if(isTransferExterne) {
				// définir le type de transfert externe
				if(transferMoneyDTO.isTransferFromExternal()) {
					log.debug("transaction: credit");

					moneyTransfer.setInternalTypeOperation(InternalTypeOperation.CREDIT);
					moneyTransfer.setDescription(InternalTypeOperation.CREDIT.toString());
				} else {
					log.debug("transaction: debit");

					moneyTransfer.setInternalTypeOperation(InternalTypeOperation.DEBIT);
					moneyTransfer.setDescription(InternalTypeOperation.DEBIT.toString());

					amountForSender *= -1;
				}

				// définir le moyen utilisé pour le transfert externe
				if(transferMoneyDTO.isTransferByBankAccount()) {
					log.debug("transaction by bank account");

					moneyTransfer.setExternalTypeOperation(ExternalTypeOperation.BANK_ACCOUNT);
				} else {
					log.debug("transaction by credit card");

					moneyTransfer.setExternalTypeOperation(ExternalTypeOperation.CREDIT_CARD);
				}

				moneyTransferRepository.save(moneyTransfer);

				log.debug("moneyTransfer = " + moneyTransfer.toString());

				// mettre à jour le compte de l'émetteur
				sender = internalBankAccountRepository.getReferenceById(userConnection.getUserSender().getInternalBankAccount().getInternalBankAccountId());

				log.debug("sender = " + sender);

				sender.setBankBalance(senderBankBalance + amountForSender);

				log.debug("sender: new bank balance = " + sender.getBankBalance());
			}
			// SI c'est une transaction interne
			else {
				log.debug("InternalTypeOperation = TRANSFER");

				moneyTransfer.setInternalTypeOperation(InternalTypeOperation.TRANSFER);
				moneyTransfer.setExternalTypeOperation(null);

				log.debug("ExternalTypeOperation = NULL");

				moneyTransferRepository.save(moneyTransfer);

				log.debug("moneyTransfer = " + moneyTransfer.toString());

				// ajouter le montant du transfert pour le destinataire
				InternalBankAccount recipient = internalBankAccountRepository.getReferenceById(userConnection.getUserRecipient().getInternalBankAccount().getInternalBankAccountId());

				log.debug("recipient = " + recipient);

				recipient.setBankBalance(recipientBankBalance + transferMoneyDTO.getAmount());

				log.debug("recipient: bank balance = " + recipient.getBankBalance());

				internalBankAccountRepository.save(recipient);

				// enlever le montant du transfert pour l'émetteur
				sender = internalBankAccountRepository.getReferenceById(userConnection.getUserSender().getInternalBankAccount().getInternalBankAccountId());

				log.debug("sender = " + sender);

				sender.setBankBalance(senderBankBalance - amountForSender);

				log.debug("sender: bank balance = " + sender.getBankBalance());
			}

			internalBankAccountRepository.save(sender);

			return ResponseForOperation.OK;
		} catch (Exception e) {
			log.error(e.toString());

			return ResponseForOperation.ERROR;
		}
	}
}
