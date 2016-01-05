package com.barclays.bbilling.web.rest.mapper;

import com.barclays.bbilling.domain.*;
import com.barclays.bbilling.web.rest.dto.CustomerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Customer and its DTO CustomerDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CustomerMapper {

    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "country.countryName", target = "countryCountryName")
    CustomerDTO customerToCustomerDTO(Customer customer);

    @Mapping(target = "customerAddresss", ignore = true)
    @Mapping(source = "countryId", target = "country")
    Customer customerDTOToCustomer(CustomerDTO customerDTO);

    default Country countryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Country country = new Country();
        country.setId(id);
        return country;
    }
}
