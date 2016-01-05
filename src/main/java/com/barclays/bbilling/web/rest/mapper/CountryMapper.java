package com.barclays.bbilling.web.rest.mapper;

import com.barclays.bbilling.domain.*;
import com.barclays.bbilling.web.rest.dto.CountryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Country and its DTO CountryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CountryMapper {

    CountryDTO countryToCountryDTO(Country country);

    @Mapping(target = "currencys", ignore = true)
    Country countryDTOToCountry(CountryDTO countryDTO);
}
