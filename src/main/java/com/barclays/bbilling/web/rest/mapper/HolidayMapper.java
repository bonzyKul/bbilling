package com.barclays.bbilling.web.rest.mapper;

import com.barclays.bbilling.domain.*;
import com.barclays.bbilling.web.rest.dto.HolidayDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Holiday and its DTO HolidayDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HolidayMapper {

    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "country.countryName", target = "countryCountryName")
    HolidayDTO holidayToHolidayDTO(Holiday holiday);

    @Mapping(source = "countryId", target = "country")
    Holiday holidayDTOToHoliday(HolidayDTO holidayDTO);

    default Country countryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Country country = new Country();
        country.setId(id);
        return country;
    }
}
