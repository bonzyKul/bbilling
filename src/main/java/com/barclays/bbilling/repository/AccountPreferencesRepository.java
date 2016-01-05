package com.barclays.bbilling.repository;

import com.barclays.bbilling.domain.AccountPreferences;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AccountPreferences entity.
 */
public interface AccountPreferencesRepository extends JpaRepository<AccountPreferences,Long> {

}
