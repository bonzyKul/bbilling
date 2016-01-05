package com.barclays.bbilling.web.rest.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.barclays.bbilling.domain.enumeration.ProductStatus;

/**
 * A DTO for the Products entity.
 */
public class ProductsDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    private String productCode;

    @NotNull
    @Size(min = 1, max = 20)
    private String productShortName;

    @NotNull
    @Size(min = 1, max = 255)
    private String productName;

    @NotNull
    private LocalDate productStartDate;

    @NotNull
    private LocalDate productEndDate;

    @NotNull
    private ProductStatus productStatus;

    private Long productFamilyId;

    private String productFamilyProductFamilyDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductShortName() {
        return productShortName;
    }

    public void setProductShortName(String productShortName) {
        this.productShortName = productShortName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public LocalDate getProductStartDate() {
        return productStartDate;
    }

    public void setProductStartDate(LocalDate productStartDate) {
        this.productStartDate = productStartDate;
    }

    public LocalDate getProductEndDate() {
        return productEndDate;
    }

    public void setProductEndDate(LocalDate productEndDate) {
        this.productEndDate = productEndDate;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    public Long getProductFamilyId() {
        return productFamilyId;
    }

    public void setProductFamilyId(Long productFamilyId) {
        this.productFamilyId = productFamilyId;
    }

    public String getProductFamilyProductFamilyDesc() {
        return productFamilyProductFamilyDesc;
    }

    public void setProductFamilyProductFamilyDesc(String productFamilyProductFamilyDesc) {
        this.productFamilyProductFamilyDesc = productFamilyProductFamilyDesc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductsDTO productsDTO = (ProductsDTO) o;

        if ( ! Objects.equals(id, productsDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProductsDTO{" +
            "id=" + id +
            ", productCode='" + productCode + "'" +
            ", productShortName='" + productShortName + "'" +
            ", productName='" + productName + "'" +
            ", productStartDate='" + productStartDate + "'" +
            ", productEndDate='" + productEndDate + "'" +
            ", productStatus='" + productStatus + "'" +
            '}';
    }
}
