package com.barclays.bbilling.Facts;

import com.barclays.bbilling.domain.Pricing;
import com.barclays.bbilling.domain.enumeration.PricingAmountType;
import com.barclays.bbilling.domain.enumeration.PricingRateType;
import com.barclays.bbilling.domain.enumeration.PricingType;
import com.barclays.bbilling.domain.enumeration.PricingUnitType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.lang.String;
import java.lang.Float;

/**
 * Created by alokkulkarni on 10/11/2015.
 */
public class PricingFact {

    private Long id;
    private PricingType pricingType;
    private BigDecimal pricingChargeAmount;
    private PricingRateType pricingRateType;
    private Integer pricingUnit;
    private Integer pricingTierFrom;
    private Integer pricingTierTo;
    private LocalDate pricingStartDate;
    private LocalDate pricingEndDate;
    private PricingUnitType pricingUnitType;
    private PricingAmountType pricingAmountType;
    private Boolean pricingForStaff;
    private Long productsId;
    private String productsProductCode;
    private Long currencyId;
    private String currencyCurrencyCode;

    public PricingFact() {

    }

    public PricingFact(PricingRateType pricingRateType, PricingType pricingType) {
        this.pricingRateType = pricingRateType;
        this.pricingType = pricingType;
    }

    public PricingFact(PricingType pricingType) {
        this.pricingType = pricingType;
    }

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

    public Integer getPricingTierFrom() {
        return pricingTierFrom;
    }

    public void setPricingTierFrom(Integer pricingTierFrom) {
        this.pricingTierFrom = pricingTierFrom;
    }

    public Integer getPricingTierTo() {
        return pricingTierTo;
    }

    public void setPricingTierTo(Integer pricingTierTo) {
        this.pricingTierTo = pricingTierTo;
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

    @Override
    public String toString() {
        return "PricingFact{" +
            "id=" + id +
            ", pricingType='" + pricingType + '\'' +
            ", pricingChargeAmount=" + pricingChargeAmount +
            ", pricingRateType='" + pricingRateType + '\'' +
            ", pricingUnit=" + pricingUnit +
            ", pricingTierFrom=" + pricingTierFrom +
            ", pricingTierTo=" + pricingTierTo +
            ", pricingStartDate=" + pricingStartDate +
            ", pricingEndDate=" + pricingEndDate +
            ", pricingUnitType='" + pricingUnitType + '\'' +
            ", pricingAmountType='" + pricingAmountType + '\'' +
            ", pricingForStaff=" + pricingForStaff +
            ", productsId=" + productsId +
            ", productsProductCode='" + productsProductCode + '\'' +
            ", currencyId=" + currencyId +
            ", currencyCurrencyCode='" + currencyCurrencyCode + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PricingFact)) return false;

        PricingFact that = (PricingFact) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getPricingType() != null ? !getPricingType().equals(that.getPricingType()) : that.getPricingType() != null)
            return false;
        if (getPricingChargeAmount() != null ? !getPricingChargeAmount().equals(that.getPricingChargeAmount()) : that.getPricingChargeAmount() != null)
            return false;
        if (getPricingRateType() != null ? !getPricingRateType().equals(that.getPricingRateType()) : that.getPricingRateType() != null)
            return false;
        if (getPricingUnit() != null ? !getPricingUnit().equals(that.getPricingUnit()) : that.getPricingUnit() != null)
            return false;
        if (getPricingTierFrom() != null ? !getPricingTierFrom().equals(that.getPricingTierFrom()) : that.getPricingTierFrom() != null)
            return false;
        if (getPricingTierTo() != null ? !getPricingTierTo().equals(that.getPricingTierTo()) : that.getPricingTierTo() != null)
            return false;
        if (getPricingStartDate() != null ? !getPricingStartDate().equals(that.getPricingStartDate()) : that.getPricingStartDate() != null)
            return false;
        if (getPricingEndDate() != null ? !getPricingEndDate().equals(that.getPricingEndDate()) : that.getPricingEndDate() != null)
            return false;
        if (getPricingUnitType() != null ? !getPricingUnitType().equals(that.getPricingUnitType()) : that.getPricingUnitType() != null)
            return false;
        if (getPricingAmountType() != null ? !getPricingAmountType().equals(that.getPricingAmountType()) : that.getPricingAmountType() != null)
            return false;
        if (getPricingForStaff() != null ? !getPricingForStaff().equals(that.getPricingForStaff()) : that.getPricingForStaff() != null)
            return false;
        if (getProductsId() != null ? !getProductsId().equals(that.getProductsId()) : that.getProductsId() != null)
            return false;
        if (getProductsProductCode() != null ? !getProductsProductCode().equals(that.getProductsProductCode()) : that.getProductsProductCode() != null)
            return false;
        if (getCurrencyId() != null ? !getCurrencyId().equals(that.getCurrencyId()) : that.getCurrencyId() != null)
            return false;
        return !(getCurrencyCurrencyCode() != null ? !getCurrencyCurrencyCode().equals(that.getCurrencyCurrencyCode()) : that.getCurrencyCurrencyCode() != null);

    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getPricingType() != null ? getPricingType().hashCode() : 0);
        result = 31 * result + (getPricingChargeAmount() != null ? getPricingChargeAmount().hashCode() : 0);
        result = 31 * result + (getPricingRateType() != null ? getPricingRateType().hashCode() : 0);
        result = 31 * result + (getPricingUnit() != null ? getPricingUnit().hashCode() : 0);
        result = 31 * result + (getPricingTierFrom() != null ? getPricingTierFrom().hashCode() : 0);
        result = 31 * result + (getPricingTierTo() != null ? getPricingTierTo().hashCode() : 0);
        result = 31 * result + (getPricingStartDate() != null ? getPricingStartDate().hashCode() : 0);
        result = 31 * result + (getPricingEndDate() != null ? getPricingEndDate().hashCode() : 0);
        result = 31 * result + (getPricingUnitType() != null ? getPricingUnitType().hashCode() : 0);
        result = 31 * result + (getPricingAmountType() != null ? getPricingAmountType().hashCode() : 0);
        result = 31 * result + (getPricingForStaff() != null ? getPricingForStaff().hashCode() : 0);
        result = 31 * result + (getProductsId() != null ? getProductsId().hashCode() : 0);
        result = 31 * result + (getProductsProductCode() != null ? getProductsProductCode().hashCode() : 0);
        result = 31 * result + (getCurrencyId() != null ? getCurrencyId().hashCode() : 0);
        result = 31 * result + (getCurrencyCurrencyCode() != null ? getCurrencyCurrencyCode().hashCode() : 0);
        return result;
    }
}
