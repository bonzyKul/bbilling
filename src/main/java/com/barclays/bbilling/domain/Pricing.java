package com.barclays.bbilling.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

import com.barclays.bbilling.domain.enumeration.PricingType;

import com.barclays.bbilling.domain.enumeration.PricingRateType;

import com.barclays.bbilling.domain.enumeration.PricingUnitType;

import com.barclays.bbilling.domain.enumeration.PricingAmountType;

/**
 * A Pricing.
 */
@Entity
@Table(name = "pricing")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "pricing")
public class Pricing implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "pricing_type", nullable = false)
    private PricingType pricingType;

    @NotNull
    @Column(name = "pricing_charge_amount", precision=10, scale=2, nullable = false)
    private BigDecimal pricingChargeAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "pricing_rate_type", nullable = false)
    private PricingRateType pricingRateType;

    @Column(name = "pricing_unit")
    private Integer pricingUnit;

    @NotNull
    @Column(name = "pricing_start_date", nullable = false)
    private LocalDate pricingStartDate;

    @NotNull
    @Column(name = "pricing_end_date", nullable = false)
    private LocalDate pricingEndDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "pricing_unit_type", nullable = false)
    private PricingUnitType pricingUnitType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "pricing_amount_type", nullable = false)
    private PricingAmountType pricingAmountType;

    @NotNull
    @Column(name = "pricing_for_staff", nullable = false)
    private Boolean pricingForStaff;

    @NotNull
    @Column(name = "pricing_tax_indicator", nullable = false)
    private Boolean pricingTaxIndicator;

    @ManyToOne
    private Products products;

    @ManyToOne
    private Currency currency;

    @OneToMany(mappedBy = "pricing")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PricingTier> pricingTiers = new HashSet<>();

    @ManyToOne
    private ServiceCatalogue serviceCatalogue;

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

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Set<PricingTier> getPricingTiers() {
        return pricingTiers;
    }

    public void setPricingTiers(Set<PricingTier> pricingTiers) {
        this.pricingTiers = pricingTiers;
    }

    public ServiceCatalogue getServiceCatalogue() {
        return serviceCatalogue;
    }

    public void setServiceCatalogue(ServiceCatalogue serviceCatalogue) {
        this.serviceCatalogue = serviceCatalogue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Pricing pricing = (Pricing) o;

        if ( ! Objects.equals(id, pricing.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Pricing{" +
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
