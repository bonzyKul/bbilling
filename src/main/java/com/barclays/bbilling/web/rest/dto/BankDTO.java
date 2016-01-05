package com.barclays.bbilling.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Bank entity.
 */
public class BankDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    private String bankCode;

    @NotNull
    @Size(min = 1, max = 100)
    private String bankName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BankDTO bankDTO = (BankDTO) o;

        if ( ! Objects.equals(id, bankDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BankDTO{" +
            "id=" + id +
            ", bankCode='" + bankCode + "'" +
            ", bankName='" + bankName + "'" +
            '}';
    }
}
