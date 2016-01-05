package com.barclays.bbilling.web.rest.mapper;

import com.barclays.bbilling.domain.*;
import com.barclays.bbilling.web.rest.dto.BankDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Bank and its DTO BankDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BankMapper {

    BankDTO bankToBankDTO(Bank bank);

    Bank bankDTOToBank(BankDTO bankDTO);
}
