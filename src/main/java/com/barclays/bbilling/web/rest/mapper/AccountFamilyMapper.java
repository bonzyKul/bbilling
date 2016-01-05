package com.barclays.bbilling.web.rest.mapper;

import com.barclays.bbilling.domain.*;
import com.barclays.bbilling.web.rest.dto.AccountFamilyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity AccountFamily and its DTO AccountFamilyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AccountFamilyMapper {

    AccountFamilyDTO accountFamilyToAccountFamilyDTO(AccountFamily accountFamily);

    @Mapping(target = "accountss", ignore = true)
    AccountFamily accountFamilyDTOToAccountFamily(AccountFamilyDTO accountFamilyDTO);
}
