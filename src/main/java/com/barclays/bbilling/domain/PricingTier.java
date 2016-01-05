package com.barclays.bbilling.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PricingTier.
 */
@Entity
@Table(name = "pricing_tier")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "pricingtier")
public class PricingTier implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "pricing_tier_from", nullable = false)
    private Long pricingTierFrom;

    @NotNull
    @Column(name = "pricing_tier_to", nullable = false)
    private Long pricingTierTo;

    @NotNull
    @Column(name = "pricing_tier_value", precision=10, scale=2, nullable = false)
    private BigDecimal pricingTierValue;

    @ManyToOne
    private Pricing pricing;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPricingTierFrom() {
        return pricingTierFrom;
    }

    public void setPricingTierFrom(Long pricingTierFrom) {
        this.pricingTierFrom = pricingTierFrom;
    }

    public Long getPricingTierTo() {
        return pricingTierTo;
    }

    public void setPricingTierTo(Long pricingTierTo) {
        this.pricingTierTo = pricingTierTo;
    }

    public BigDecimal getPricingTierValue() {
        return pricingTierValue;
    }

    public void setPricingTierValue(BigDecimal pricingTierValue) {
        this.pricingTierValue = pricingTierValue;
    }

    public Pricing getPricing() {
        return pricing;
    }

    public void setPricing(Pricing pricing) {
        this.pricing = pricing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PricingTier pricingTier = (PricingTier) o;

        if ( ! Objects.equals(id, pricingTier.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PricingTier{" +
            "id=" + id +
            ", pricingTierFrom='" + pricingTierFrom + "'" +
            ", pricingTierTo='" + pricingTierTo + "'" +
            ", pricingTierValue='" + pricingTierValue + "'" +
            '}';
    }
}
