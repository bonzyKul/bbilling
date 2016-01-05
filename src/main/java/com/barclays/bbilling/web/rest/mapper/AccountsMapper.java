package com.barclays.bbilling.web.rest.mapper;

import com.barclays.bbilling.domain.*;
import com.barclays.bbilling.web.rest.dto.AccountsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Accounts and its DTO AccountsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AccountsMapper {

    @Mapping(source = "products.id", target = "productsId")
    @Mapping(source = "products.productName", target = "productsProductName")
    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(source = "branch.branchName", target = "branchBranchName")
    @Mapping(source = "bank.id", target = "bankId")
    @Mapping(source = "bank.bankName", target = "bankBankName")
    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "country.countryName", target = "countryCountryName")
    @Mapping(source = "currency.id", target = "currencyId")
    @Mapping(source = "currency.currencyDescription", target = "currencyCurrencyDescription")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.customerID", target = "customerCustomerID")
    @Mapping(source = "accountFamily.id", target = "accountFamilyId")
    @Mapping(source = "accountFamily.accountFamilyDesc", target = "accountFamilyAccountFamilyDesc")
    AccountsDTO accountsToAccountsDTO(Accounts accounts);

    @Mapping(source = "productsId", target = "products")
    @Mapping(source = "branchId", target = "branch")
    @Mapping(source = "bankId", target = "bank")
    @Mapping(source = "countryId", target = "country")
    @Mapping(source = "currencyId", target = "currency")
    @Mapping(source = "customerId", target = "customer")
    @Mapping(source = "accountFamilyId", target = "accountFamily")
    Accounts accountsDTOToAccounts(AccountsDTO accountsDTO);

    default Products productsFromId(Long id) {
        if (id == null) {
            return null;
        }
        Products products = new Products();
        products.setId(id);
        return products;
    }

    default Branch branchFromId(Long id) {
        if (id == null) {
            return null;
        }
        Branch branch = new Branch();
        branch.setId(id);
        return branch;
    }

    default Bank bankFromId(Long id) {
        if (id == null) {
            return null;
        }
        Bank bank = new Bank();
        bank.setId(id);
        return bank;
    }

    default Country countryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Country country = new Country();
        country.setId(id);
        return country;
    }

    default Currency currencyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Currency currency = new Currency();
        currency.setId(id);
        return currency;
    }

    default Customer customerFromId(Long id) {
        if (id == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
    }

    default AccountFamily accountFamilyFromId(Long id) {
        if (id == null) {
            return null;
        }
        AccountFamily accountFamily = new AccountFamily();
        accountFamily.setId(id);
        return accountFamily;
    }
}
