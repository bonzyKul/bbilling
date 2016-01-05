package com.barclays.bbilling.web.rest.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the AccountAppHLD entity.
 */
public class AccountAppHLDDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate appHLDStartDate;

    @NotNull
    private LocalDate appHLDEndDate;

    private Long accountsId;

    private String accountsAccountNumber;

    private Long productsId;

    private String productsProductName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getAppHLDStartDate() {
        return appHLDStartDate;
    }

    public void setAppHLDStartDate(LocalDate appHLDStartDate) {
        this.appHLDStartDate = appHLDStartDate;
    }

    public LocalDate getAppHLDEndDate() {
        return appHLDEndDate;
    }

    public void setAppHLDEndDate(LocalDate appHLDEndDate) {
        this.appHLDEndDate = appHLDEndDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccountAppHLDDTO accountAppHLDDTO = (AccountAppHLDDTO) o;

        if ( ! Objects.equals(id, accountAppHLDDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AccountAppHLDDTO{" +
            "id=" + id +
            ", appHLDStartDate='" + appHLDStartDate + "'" +
            ", appHLDEndDate='" + appHLDEndDate + "'" +
            '}';
    }
}
