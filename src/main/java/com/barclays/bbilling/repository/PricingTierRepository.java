package com.barclays.bbilling.repository;

import com.barclays.bbilling.domain.PricingTier;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PricingTier entity.
 */
public interface PricingTierRepository extends JpaRepository<PricingTier,Long> {

}
