package com.barclays.bbilling.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
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
 * A Accounts.
 */
@Entity
@Table(name = "accounts")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "accounts")
public class Accounts implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "account_number", length = 100, nullable = false)
    private String accountNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @NotNull
    @Column(name = "account_opened_date", nullable = false)
    private LocalDate accountOpenedDate;

    @NotNull
    @Column(name = "account_closed_date", nullable = false)
    private LocalDate accountClosedDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false)
    private AccountStatus accountStatus;

    @NotNull
    @Column(name = "account_crturn_over", precision=10, scale=2, nullable = false)
    private BigDecimal accountCRTurnOver;

    @NotNull
    @Column(name = "account_drturn_over", precision=10, scale=2, nullable = false)
    private BigDecimal accountDRTurnOver;

    @NotNull
    @Column(name = "account_avail_bal", precision=10, scale=2, nullable = false)
    private BigDecimal accountAvailBal;

    @Column(name = "account_ledger_bal", precision=10, scale=2, nullable = false)
    private BigDecimal accountLedgerBal;

    @Column(name = "account_balance", precision=10, scale=2, nullable = false)
    private BigDecimal accountBalance;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "account_last_bal_type", nullable = false)
    private AccountLastBalType accountLastBalType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "account_tier", nullable = false)
    private AccountTier accountTier;

    @Column(name = "account_charging_bal", precision=10, scale=2, nullable = false)
    private BigDecimal accountChargingBal;

    @OneToOne    private Products products;

    @ManyToOne
    private Branch branch;

    @ManyToOne
    private Bank bank;

    @ManyToOne
    private Country country;

    @ManyToOne
    private Currency currency;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private AccountFamily accountFamily;

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

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public AccountFamily getAccountFamily() {
        return accountFamily;
    }

    public void setAccountFamily(AccountFamily accountFamily) {
        this.accountFamily = accountFamily;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Accounts accounts = (Accounts) o;

        if ( ! Objects.equals(id, accounts.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Accounts{" +
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
