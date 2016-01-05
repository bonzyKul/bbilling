package com.barclays.bbilling.repository;

import com.barclays.bbilling.domain.ProductFamily;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProductFamily entity.
 */
public interface ProductFamilyRepository extends JpaRepository<ProductFamily,Long> {

}
