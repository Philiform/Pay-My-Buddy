package com.paymybuddy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.paymybuddy.model.UserConnection;

// TODO: Auto-generated Javadoc
/**
 * The Interface UserConnectionRepository.
 */
public interface UserConnectionRepository extends JpaRepository<UserConnection, Integer>{

	/**
	 * Find by user sender not deleted.
	 *
	 * @param userSender the user sender
	 * @return the list
	 */
	@Query(value = "SELECT * FROM user_connection WHERE user_id_sender = :userSender AND deleted = 0", nativeQuery = true)
	List<UserConnection> findByUserSenderNotDeleted(final int userSender);

	/**
	 * Find user connection id by user sender.
	 *
	 * @param userSender the user sender
	 * @return the list
	 */
	@Query(value = "SELECT user_connection_id FROM user_connection WHERE user_id_sender = :userSender", nativeQuery = true)
	List<Integer> findUserConnectionIdByUserSender(final int userSender);

	/**
	 * Find pseudo by user sender and recipient not deleted.
	 *
	 * @param userSender the user sender
	 * @return the list
	 */
	@Query(value = "SELECT pseudo FROM user_connection WHERE user_id_sender = :userSender AND deleted = 0 ORDER BY pseudo ASC", nativeQuery = true)
	List<String> findPseudoByUserSenderAndRecipientNotDeleted(final int userSender);

	/**
	 * Find by user sender and not deleted order by pseudo.
	 *
	 * @param userSender the user sender
	 * @return the list
	 */
	@Query(value = "SELECT * FROM user_connection WHERE user_id_sender = :userSender AND deleted = 0 ORDER BY pseudo ASC", nativeQuery = true)
	List<UserConnection> findByUserSenderAndNotDeletedOrderByPseudo(final int userSender);

	/**
	 * Find to delete.
	 *
	 * @param userSender the user sender
	 * @return the list
	 */
	@Query(value = "SELECT * FROM user_connection WHERE (user_id_sender = :userSender OR user_id_recipient = :userSender)AND deleted = 0", nativeQuery = true)
	List<UserConnection> findToDelete(final int userSender);

}
