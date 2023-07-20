package com.paymybuddy.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.paymybuddy.model.MoneyTransfer;

public interface MoneyTransferRepository extends JpaRepository<MoneyTransfer, Integer>{

	@Query(value = "SELECT * FROM money_transfer WHERE user_connection_id = :userConnection", nativeQuery = true)
	Page<MoneyTransfer> findByUserConnection(int userConnection, Pageable pageable);

	@Query(value = "SELECT * FROM money_transfer WHERE user_connection_id IN (:listUserConnection) ORDER BY date DESC", nativeQuery = true)
	Page<MoneyTransfer> findByListUserConnection(List<String> listUserConnection, Pageable pageable);

	@Query(value = "SELECT SUM(commission) FROM money_transfer", nativeQuery = true)
	float getTotalCommissions();

}
