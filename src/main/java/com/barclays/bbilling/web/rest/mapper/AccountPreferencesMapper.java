package com.barclays.bbilling.web.rest.mapper;

import com.barclays.bbilling.domain.*;
import com.barclays.bbilling.web.rest.dto.AccountPreferencesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity AccountPreferences and its DTO AccountPreferencesDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AccountPreferencesMapper {

    @Mapping(source = "accounts.id", target = "accountsId")
    @Mapping(source = "accounts.accountNumber", target = "accountsAccountNumber")
    AccountPreferencesDTO accountPreferencesToAccountPreferencesDTO(AccountPreferences accountPreferences);

    @Mapping(source = "accountsId", target = "accounts")
    AccountPreferences accountPreferencesDTOToAccountPreferences(AccountPreferencesDTO accountPreferencesDTO);

    default Accounts accountsFromId(Long id) {
        if (id == null) {
            return null;
        }
        Accounts accounts = new Accounts();
        accounts.setId(id);
        return accounts;
    }
}
