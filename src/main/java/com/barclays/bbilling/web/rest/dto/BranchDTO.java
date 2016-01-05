package com.barclays.bbilling.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Branch entity.
 */
public class BranchDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    private String branchCode;

    @NotNull
    @Size(min = 1, max = 100)
    private String branchName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BranchDTO branchDTO = (BranchDTO) o;

        if ( ! Objects.equals(id, branchDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BranchDTO{" +
            "id=" + id +
            ", branchCode='" + branchCode + "'" +
            ", branchName='" + branchName + "'" +
            '}';
    }
}
