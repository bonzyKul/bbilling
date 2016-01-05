package com.barclays.bbilling.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the ProductFamily entity.
 */
public class ProductFamilyDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    private String productFamilyCode;

    @NotNull
    @Size(min = 1, max = 255)
    private String productFamilyDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductFamilyCode() {
        return productFamilyCode;
    }

    public void setProductFamilyCode(String productFamilyCode) {
        this.productFamilyCode = productFamilyCode;
    }

    public String getProductFamilyDesc() {
        return productFamilyDesc;
    }

    public void setProductFamilyDesc(String productFamilyDesc) {
        this.productFamilyDesc = productFamilyDesc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductFamilyDTO productFamilyDTO = (ProductFamilyDTO) o;

        if ( ! Objects.equals(id, productFamilyDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProductFamilyDTO{" +
            "id=" + id +
            ", productFamilyCode='" + productFamilyCode + "'" +
            ", productFamilyDesc='" + productFamilyDesc + "'" +
            '}';
    }
}
