package com.barclays.bbilling.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the AccountFamily entity.
 */
public class AccountFamilyDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    private String accountFamilyCode;

    @NotNull
    @Size(min = 1, max = 255)
    private String accountFamilyDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountFamilyCode() {
        return accountFamilyCode;
    }

    public void setAccountFamilyCode(String accountFamilyCode) {
        this.accountFamilyCode = accountFamilyCode;
    }

    public String getAccountFamilyDesc() {
        return accountFamilyDesc;
    }

    public void setAccountFamilyDesc(String accountFamilyDesc) {
        this.accountFamilyDesc = accountFamilyDesc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccountFamilyDTO accountFamilyDTO = (AccountFamilyDTO) o;

        if ( ! Objects.equals(id, accountFamilyDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AccountFamilyDTO{" +
            "id=" + id +
            ", accountFamilyCode='" + accountFamilyCode + "'" +
            ", accountFamilyDesc='" + accountFamilyDesc + "'" +
            '}';
    }
}
