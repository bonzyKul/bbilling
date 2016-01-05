package com.barclays.bbilling.repository;

import com.barclays.bbilling.domain.AccountFamily;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AccountFamily entity.
 */
public interface AccountFamilyRepository extends JpaRepository<AccountFamily,Long> {

}
