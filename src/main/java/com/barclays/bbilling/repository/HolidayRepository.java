package com.barclays.bbilling.repository;

import com.barclays.bbilling.domain.Holiday;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Holiday entity.
 */
public interface HolidayRepository extends JpaRepository<Holiday,Long> {

}
