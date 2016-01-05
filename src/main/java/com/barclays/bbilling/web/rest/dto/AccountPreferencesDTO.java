package com.barclays.bbilling.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the AccountPreferences entity.
 */
public class AccountPreferencesDTO implements Serializable {

    private Long id;

    @NotNull
    private Boolean accountEStatement;

    @NotNull
    private Boolean accountNotification;

    @NotNull
    private Boolean accountLvlBilling;

    @NotNull
    private Boolean accountPrintStmt;

    @NotNull
    private Boolean accountRewardSuppInd;

    @NotNull
    private Integer accountPackCnt;

    @NotNull
    private Boolean accountCFInd;

    @NotNull
    private Boolean accountPNCS;

    @NotNull
    private Boolean accountBillingDefault;

    private Long accountsId;

    private String accountsAccountNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAccountEStatement() {
        return accountEStatement;
    }

    public void setAccountEStatement(Boolean accountEStatement) {
        this.accountEStatement = accountEStatement;
    }

    public Boolean getAccountNotification() {
        return accountNotification;
    }

    public void setAccountNotification(Boolean accountNotification) {
        this.accountNotification = accountNotification;
    }

    public Boolean getAccountLvlBilling() {
        return accountLvlBilling;
    }

    public void setAccountLvlBilling(Boolean accountLvlBilling) {
        this.accountLvlBilling = accountLvlBilling;
    }

    public Boolean getAccountPrintStmt() {
        return accountPrintStmt;
    }

    public void setAccountPrintStmt(Boolean accountPrintStmt) {
        this.accountPrintStmt = accountPrintStmt;
    }

    public Boolean getAccountRewardSuppInd() {
        return accountRewardSuppInd;
    }

    public void setAccountRewardSuppInd(Boolean accountRewardSuppInd) {
        this.accountRewardSuppInd = accountRewardSuppInd;
    }

    public Integer getAccountPackCnt() {
        return accountPackCnt;
    }

    public void setAccountPackCnt(Integer accountPackCnt) {
        this.accountPackCnt = accountPackCnt;
    }

    public Boolean getAccountCFInd() {
        return accountCFInd;
    }

    public void setAccountCFInd(Boolean accountCFInd) {
        this.accountCFInd = accountCFInd;
    }

    public Boolean getAccountPNCS() {
        return accountPNCS;
    }

    public void setAccountPNCS(Boolean accountPNCS) {
        this.accountPNCS = accountPNCS;
    }

    public Boolean getAccountBillingDefault() {
        return accountBillingDefault;
    }

    public void setAccountBillingDefault(Boolean accountBillingDefault) {
        this.accountBillingDefault = accountBillingDefault;
    }

    public Long getAccountsId() {
        return accountsId;
    }

    public void setAccountsId(Long accountsId) {
        this.accountsId = accountsId;
    }

    public String getAccountsAccountNumber() {
        return accountsAccountNumber;
    }

    public void setAccountsAccountNumber(String accountsAccountNumber) {
        this.accountsAccountNumber = accountsAccountNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccountPreferencesDTO accountPreferencesDTO = (AccountPreferencesDTO) o;

        if ( ! Objects.equals(id, accountPreferencesDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AccountPreferencesDTO{" +
            "id=" + id +
            ", accountEStatement='" + accountEStatement + "'" +
            ", accountNotification='" + accountNotification + "'" +
            ", accountLvlBilling='" + accountLvlBilling + "'" +
            ", accountPrintStmt='" + accountPrintStmt + "'" +
            ", accountRewardSuppInd='" + accountRewardSuppInd + "'" +
            ", accountPackCnt='" + accountPackCnt + "'" +
            ", accountCFInd='" + accountCFInd + "'" +
            ", accountPNCS='" + accountPNCS + "'" +
            ", accountBillingDefault='" + accountBillingDefault + "'" +
            '}';
    }
}
