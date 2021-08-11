/*
 * @(#) PaymentRepository.java 2021. 08. 10.
 *
 * Created by cooingpop
 */

package com.cooingpop.apiclient.api.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cooingpop.apiclient.api.order.domain.Payment;

/**
 * @author 박준영
 **/
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
	@Query("select p from Payment p where p.userSeq = :userSeq")
	List<Payment> findAllPaymentByUserSeq(@Param("userSeq") Long userSeq);
}

