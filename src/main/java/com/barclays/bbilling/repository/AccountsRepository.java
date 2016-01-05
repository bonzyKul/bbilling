package com.barclays.bbilling.repository;

import com.barclays.bbilling.domain.Accounts;

import org.springframework.data.jpa.repository.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the Accounts entity.
 */
public interface AccountsRepository extends JpaRepository<Accounts,Long> {

    List<Accounts> findAllByAccountOpenedDate(LocalDate accountOpenedDate);
    Accounts findOneByAccountNumber(String account);

}
