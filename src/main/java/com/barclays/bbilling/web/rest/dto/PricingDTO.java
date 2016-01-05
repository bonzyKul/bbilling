package com.barclays.bbilling.web.rest.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.barclays.bbilling.domain.enumeration.PricingType;
import com.barclays.bbilling.domain.enumeration.PricingRateType;
import com.barclays.bbilling.domain.enumeration.PricingUnitType;
import com.barclays.bbilling.domain.enumeration.PricingAmountType;

/**
 * A DTO for the Pricing entity.
 */
public class PricingDTO implements Serializable {

    private Long id;

    @NotNull
    private PricingType pricingType;

    @NotNull
    private BigDecimal pricingChargeAmount;

    @NotNull
    private PricingRateType pricingRateType;

    private Integer pricingUnit;

    @NotNull
    private LocalDate pricingStartDate;

    @NotNull
    private LocalDate pricingEndDate;

    @NotNull
    private PricingUnitType pricingUnitType;

    @NotNull
    private PricingAmountType pricingAmountType;

    @NotNull
    private Boolean pricingForStaff;

    @NotNull
    private Boolean pricingTaxIndicator;

    private Long productsId;

    private String productsProductCode;

    private Long currencyId;

    private String currencyCurrencyCode;

    private Long serviceCatalogueId;

    private String serviceCatalogueServiceCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PricingType getPricingType() {
        return pricingType;
    }

    public void setPricingType(PricingType pricingType) {
        this.pricingType = pricingType;
    }

    public BigDecimal getPricingChargeAmount() {
        return pricingChargeAmount;
    }

    public void setPricingChargeAmount(BigDecimal pricingChargeAmount) {
        this.pricingChargeAmount = pricingChargeAmount;
    }

    public PricingRateType getPricingRateType() {
        return pricingRateType;
    }

    public void setPricingRateType(PricingRateType pricingRateType) {
        this.pricingRateType = pricingRateType;
    }

    public Integer getPricingUnit() {
        return pricingUnit;
    }

    public void setPricingUnit(Integer pricingUnit) {
        this.pricingUnit = pricingUnit;
    }

    public LocalDate getPricingStartDate() {
        return pricingStartDate;
    }

    public void setPricingStartDate(LocalDate pricingStartDate) {
        this.pricingStartDate = pricingStartDate;
    }

    public LocalDate getPricingEndDate() {
        return pricingEndDate;
    }

    public void setPricingEndDate(LocalDate pricingEndDate) {
        this.pricingEndDate = pricingEndDate;
    }

    public PricingUnitType getPricingUnitType() {
        return pricingUnitType;
    }

    public void setPricingUnitType(PricingUnitType pricingUnitType) {
        this.pricingUnitType = pricingUnitType;
    }

    public PricingAmountType getPricingAmountType() {
        return pricingAmountType;
    }

    public void setPricingAmountType(PricingAmountType pricingAmountType) {
        this.pricingAmountType = pricingAmountType;
    }

    public Boolean getPricingForStaff() {
        return pricingForStaff;
    }

    public void setPricingForStaff(Boolean pricingForStaff) {
        this.pricingForStaff = pricingForStaff;
    }

    public Boolean getPricingTaxIndicator() {
        return pricingTaxIndicator;
    }

    public void setPricingTaxIndicator(Boolean pricingTaxIndicator) {
        this.pricingTaxIndicator = pricingTaxIndicator;
    }

    public Long getProductsId() {
        return productsId;
    }

    public void setProductsId(Long productsId) {
        this.productsId = productsId;
    }

    public String getProductsProductCode() {
        return productsProductCode;
    }

    public void setProductsProductCode(String productsProductCode) {
        this.productsProductCode = productsProductCode;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyCurrencyCode() {
        return currencyCurrencyCode;
    }

    public void setCurrencyCurrencyCode(String currencyCurrencyCode) {
        this.currencyCurrencyCode = currencyCurrencyCode;
    }

    public Long getServiceCatalogueId() {
        return serviceCatalogueId;
    }

    public void setServiceCatalogueId(Long serviceCatalogueId) {
        this.serviceCatalogueId = serviceCatalogueId;
    }

    public String getServiceCatalogueServiceCode() {
        return serviceCatalogueServiceCode;
    }

    public void setServiceCatalogueServiceCode(String serviceCatalogueServiceCode) {
        this.serviceCatalogueServiceCode = serviceCatalogueServiceCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PricingDTO pricingDTO = (PricingDTO) o;

        if ( ! Objects.equals(id, pricingDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PricingDTO{" +
            "id=" + id +
            ", pricingType='" + pricingType + "'" +
            ", pricingChargeAmount='" + pricingChargeAmount + "'" +
            ", pricingRateType='" + pricingRateType + "'" +
            ", pricingUnit='" + pricingUnit + "'" +
            ", pricingStartDate='" + pricingStartDate + "'" +
            ", pricingEndDate='" + pricingEndDate + "'" +
            ", pricingUnitType='" + pricingUnitType + "'" +
            ", pricingAmountType='" + pricingAmountType + "'" +
            ", pricingForStaff='" + pricingForStaff + "'" +
            ", pricingTaxIndicator='" + pricingTaxIndicator + "'" +
            '}';
    }
}
