package com.barclays.bbilling.repository;

import com.barclays.bbilling.domain.ServiceCatalogue;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ServiceCatalogue entity.
 */
public interface ServiceCatalogueRepository extends JpaRepository<ServiceCatalogue,Long> {

}
