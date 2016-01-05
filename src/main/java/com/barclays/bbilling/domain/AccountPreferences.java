package com.barclays.bbilling.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A AccountPreferences.
 */
@Entity
@Table(name = "account_preferences")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "accountpreferences")
public class AccountPreferences implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "account_estatement", nullable = false)
    private Boolean accountEStatement;

    @NotNull
    @Column(name = "account_notification", nullable = false)
    private Boolean accountNotification;

    @NotNull
    @Column(name = "account_lvl_billing", nullable = false)
    private Boolean accountLvlBilling;

    @NotNull
    @Column(name = "account_print_stmt", nullable = false)
    private Boolean accountPrintStmt;

    @NotNull
    @Column(name = "account_reward_supp_ind", nullable = false)
    private Boolean accountRewardSuppInd;

    @NotNull
    @Column(name = "account_pack_cnt", nullable = false)
    private Integer accountPackCnt;

    @NotNull
    @Column(name = "account_cfind", nullable = false)
    private Boolean accountCFInd;

    @NotNull
    @Column(name = "account_pncs", nullable = false)
    private Boolean accountPNCS;

    @NotNull
    @Column(name = "account_billing_default", nullable = false)
    private Boolean accountBillingDefault;

    @ManyToOne
    private Accounts accounts;

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

    public Accounts getAccounts() {
        return accounts;
    }

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccountPreferences accountPreferences = (AccountPreferences) o;

        if ( ! Objects.equals(id, accountPreferences.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AccountPreferences{" +
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
