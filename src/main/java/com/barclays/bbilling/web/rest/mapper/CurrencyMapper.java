package com.barclays.bbilling.web.rest.mapper;

import com.barclays.bbilling.domain.*;
import com.barclays.bbilling.web.rest.dto.CurrencyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Currency and its DTO CurrencyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CurrencyMapper {

    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "country.countryName", target = "countryCountryName")
    CurrencyDTO currencyToCurrencyDTO(Currency currency);

    @Mapping(source = "countryId", target = "country")
    Currency currencyDTOToCurrency(CurrencyDTO currencyDTO);

    default Country countryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Country country = new Country();
        country.setId(id);
        return country;
    }
}
