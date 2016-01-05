package com.barclays.bbilling.repository;

import com.barclays.bbilling.domain.Pricing;

import com.barclays.bbilling.domain.Products;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Pricing entity.
 */
public interface PricingRepository extends JpaRepository<Pricing,Long> {


    Pricing findOneByProductsAndPricingForStaff(Products products, Boolean pricingForStaff);

}
