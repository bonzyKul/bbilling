package com.barclays.bbilling.web.rest.mapper;

import com.barclays.bbilling.domain.*;
import com.barclays.bbilling.web.rest.dto.CustomerAddressDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CustomerAddress and its DTO CustomerAddressDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CustomerAddressMapper {

    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "country.countryName", target = "countryCountryName")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.customerID", target = "customerCustomerID")
    CustomerAddressDTO customerAddressToCustomerAddressDTO(CustomerAddress customerAddress);

    @Mapping(source = "countryId", target = "country")
    @Mapping(source = "customerId", target = "customer")
    CustomerAddress customerAddressDTOToCustomerAddress(CustomerAddressDTO customerAddressDTO);

    default Country countryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Country country = new Country();
        country.setId(id);
        return country;
    }

    default Customer customerFromId(Long id) {
        if (id == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
    }
}
