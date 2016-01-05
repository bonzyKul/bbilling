package com.barclays.bbilling.repository;

import com.barclays.bbilling.domain.Bank;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Bank entity.
 */
public interface BankRepository extends JpaRepository<Bank,Long> {

}
