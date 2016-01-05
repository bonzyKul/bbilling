package com.barclays.bbilling.web.rest.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.barclays.bbilling.domain.enumeration.AccountType;
import com.barclays.bbilling.domain.enumeration.AccountStatus;
import com.barclays.bbilling.domain.enumeration.AccountLastBalType;
import com.barclays.bbilling.domain.enumeration.AccountTier;

/**
 * A DTO for the Accounts entity.
 */
public class AccountsDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    private String accountNumber;

    @NotNull
    private AccountType accountType;

    @NotNull
    private LocalDate accountOpenedDate;

    @NotNull
    private LocalDate accountClosedDate;

    @NotNull
    private AccountStatus accountStatus;

    @NotNull
    private BigDecimal accountCRTurnOver;

    @NotNull
    private BigDecimal accountDRTurnOver;

    @NotNull
    private BigDecimal accountAvailBal;

    private BigDecimal accountLedgerBal;

    private BigDecimal accountBalance;

    @NotNull
    private AccountLastBalType accountLastBalType;

    @NotNull
    private AccountTier accountTier;

    private BigDecimal accountChargingBal;

    private Long productsId;

    private String productsProductName;

    private Long branchId;

    private String branchBranchName;

    private Long bankId;

    private String bankBankName;

    private Long countryId;

    private String countryCountryName;

    private Long currencyId;

    private String currencyCurrencyDescription;

    private Long customerId;

    private String customerCustomerID;

    private Long accountFamilyId;

    private String accountFamilyAccountFamilyDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public LocalDate getAccountOpenedDate() {
        return accountOpenedDate;
    }

    public void setAccountOpenedDate(LocalDate accountOpenedDate) {
        this.accountOpenedDate = accountOpenedDate;
    }

    public LocalDate getAccountClosedDate() {
        return accountClosedDate;
    }

    public void setAccountClosedDate(LocalDate accountClosedDate) {
        this.accountClosedDate = accountClosedDate;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public BigDecimal getAccountCRTurnOver() {
        return accountCRTurnOver;
    }

    public void setAccountCRTurnOver(BigDecimal accountCRTurnOver) {
        this.accountCRTurnOver = accountCRTurnOver;
    }

    public BigDecimal getAccountDRTurnOver() {
        return accountDRTurnOver;
    }

    public void setAccountDRTurnOver(BigDecimal accountDRTurnOver) {
        this.accountDRTurnOver = accountDRTurnOver;
    }

    public BigDecimal getAccountAvailBal() {
        return accountAvailBal;
    }

    public void setAccountAvailBal(BigDecimal accountAvailBal) {
        this.accountAvailBal = accountAvailBal;
    }

    public BigDecimal getAccountLedgerBal() {
        return accountLedgerBal;
    }

    public void setAccountLedgerBal(BigDecimal accountLedgerBal) {
        this.accountLedgerBal = accountLedgerBal;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public AccountLastBalType getAccountLastBalType() {
        return accountLastBalType;
    }

    public void setAccountLastBalType(AccountLastBalType accountLastBalType) {
        this.accountLastBalType = accountLastBalType;
    }

    public AccountTier getAccountTier() {
        return accountTier;
    }

    public void setAccountTier(AccountTier accountTier) {
        this.accountTier = accountTier;
    }

    public BigDecimal getAccountChargingBal() {
        return accountChargingBal;
    }

    public void setAccountChargingBal(BigDecimal accountChargingBal) {
        this.accountChargingBal = accountChargingBal;
    }

    public Long getProductsId() {
        return productsId;
    }

    public void setProductsId(Long productsId) {
        this.productsId = productsId;
    }

    public String getProductsProductName() {
        return productsProductName;
    }

    public void setProductsProductName(String productsProductName) {
        this.productsProductName = productsProductName;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getBranchBranchName() {
        return branchBranchName;
    }

    public void setBranchBranchName(String branchBranchName) {
        this.branchBranchName = branchBranchName;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public String getBankBankName() {
        return bankBankName;
    }

    public void setBankBankName(String bankBankName) {
        this.bankBankName = bankBankName;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getCountryCountryName() {
        return countryCountryName;
    }

    public void setCountryCountryName(String countryCountryName) {
        this.countryCountryName = countryCountryName;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyCurrencyDescription() {
        return currencyCurrencyDescription;
    }

    public void setCurrencyCurrencyDescription(String currencyCurrencyDescription) {
        this.currencyCurrencyDescription = currencyCurrencyDescription;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCustomerID() {
        return customerCustomerID;
    }

    public void setCustomerCustomerID(String customerCustomerID) {
        this.customerCustomerID = customerCustomerID;
    }

    public Long getAccountFamilyId() {
        return accountFamilyId;
    }

    public void setAccountFamilyId(Long accountFamilyId) {
        this.accountFamilyId = accountFamilyId;
    }

    public String getAccountFamilyAccountFamilyDesc() {
        return accountFamilyAccountFamilyDesc;
    }

    public void setAccountFamilyAccountFamilyDesc(String accountFamilyAccountFamilyDesc) {
        this.accountFamilyAccountFamilyDesc = accountFamilyAccountFamilyDesc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccountsDTO accountsDTO = (AccountsDTO) o;

        if ( ! Objects.equals(id, accountsDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AccountsDTO{" +
            "id=" + id +
            ", accountNumber='" + accountNumber + "'" +
            ", accountType='" + accountType + "'" +
            ", accountOpenedDate='" + accountOpenedDate + "'" +
            ", accountClosedDate='" + accountClosedDate + "'" +
            ", accountStatus='" + accountStatus + "'" +
            ", accountCRTurnOver='" + accountCRTurnOver + "'" +
            ", accountDRTurnOver='" + accountDRTurnOver + "'" +
            ", accountAvailBal='" + accountAvailBal + "'" +
            ", accountLedgerBal='" + accountLedgerBal + "'" +
            ", accountBalance='" + accountBalance + "'" +
            ", accountLastBalType='" + accountLastBalType + "'" +
            ", accountTier='" + accountTier + "'" +
            ", accountChargingBal='" + accountChargingBal + "'" +
            '}';
    }
}
