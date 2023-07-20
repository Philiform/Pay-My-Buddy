package com.paymybuddy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.paymybuddy.model.UserConnection;

public interface UserConnectionRepository extends JpaRepository<UserConnection, Integer>{

	@Query(value = "SELECT * FROM user_connection WHERE user_id_sender = :userSender AND deleted = 0", nativeQuery = true)
	List<UserConnection> findByUserSenderNotDeleted(final int userSender);

	@Query(value = "SELECT user_connection_id FROM user_connection WHERE user_id_sender = :userSender", nativeQuery = true)
	List<Integer> findUserConnectionIdByUserSender(final int userSender);

	@Query(value = "SELECT pseudo FROM user_connection WHERE user_id_sender = :userSender AND deleted = 0 ORDER BY pseudo ASC", nativeQuery = true)
	List<String> findPseudoByUserSenderAndRecipientNotDeleted(final int userSender);

	@Query(value = "SELECT * FROM user_connection WHERE user_id_sender = :userSender AND deleted = 0 ORDER BY pseudo ASC", nativeQuery = true)
	List<UserConnection> findByUserSenderAndNotDeletedOrderByPseudo(final int userSender);

	@Query(value = "SELECT * FROM user_connection WHERE (user_id_sender = :userSender OR user_id_recipient = :userSender)AND deleted = 0", nativeQuery = true)
	List<UserConnection> findToDelete(final int userSender);

}
