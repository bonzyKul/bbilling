package com.barclays.bbilling.repository;

import com.barclays.bbilling.domain.CustomerAddress;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CustomerAddress entity.
 */
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress,Long> {

}
