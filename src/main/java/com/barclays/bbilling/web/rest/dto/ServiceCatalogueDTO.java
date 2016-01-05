package com.barclays.bbilling.web.rest.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the ServiceCatalogue entity.
 */
public class ServiceCatalogueDTO implements Serializable {

    private Long id;

    @NotNull
    private String serviceCode;

    @NotNull
    private String serviceDescription;

    @NotNull
    private LocalDate serviceStartDate;

    @NotNull
    private LocalDate serviceEndDate;

    private Long productsId;

    private String productsProductName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public LocalDate getServiceStartDate() {
        return serviceStartDate;
    }

    public void setServiceStartDate(LocalDate serviceStartDate) {
        this.serviceStartDate = serviceStartDate;
    }

    public LocalDate getServiceEndDate() {
        return serviceEndDate;
    }

    public void setServiceEndDate(LocalDate serviceEndDate) {
        this.serviceEndDate = serviceEndDate;
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

        ServiceCatalogueDTO serviceCatalogueDTO = (ServiceCatalogueDTO) o;

        if ( ! Objects.equals(id, serviceCatalogueDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ServiceCatalogueDTO{" +
            "id=" + id +
            ", serviceCode='" + serviceCode + "'" +
            ", serviceDescription='" + serviceDescription + "'" +
            ", serviceStartDate='" + serviceStartDate + "'" +
            ", serviceEndDate='" + serviceEndDate + "'" +
            '}';
    }
}
