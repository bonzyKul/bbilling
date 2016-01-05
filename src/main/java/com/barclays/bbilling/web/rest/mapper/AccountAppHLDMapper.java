package com.barclays.bbilling.web.rest.mapper;

import com.barclays.bbilling.domain.*;
import com.barclays.bbilling.web.rest.dto.AccountAppHLDDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity AccountAppHLD and its DTO AccountAppHLDDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AccountAppHLDMapper {

    @Mapping(source = "accounts.id", target = "accountsId")
    @Mapping(source = "accounts.accountNumber", target = "accountsAccountNumber")
    @Mapping(source = "products.id", target = "productsId")
    @Mapping(source = "products.productName", target = "productsProductName")
    AccountAppHLDDTO accountAppHLDToAccountAppHLDDTO(AccountAppHLD accountAppHLD);

    @Mapping(source = "accountsId", target = "accounts")
    @Mapping(source = "productsId", target = "products")
    AccountAppHLD accountAppHLDDTOToAccountAppHLD(AccountAppHLDDTO accountAppHLDDTO);

    default Accounts accountsFromId(Long id) {
        if (id == null) {
            return null;
        }
        Accounts accounts = new Accounts();
        accounts.setId(id);
        return accounts;
    }

    default Products productsFromId(Long id) {
        if (id == null) {
            return null;
        }
        Products products = new Products();
        products.setId(id);
        return products;
    }
}
