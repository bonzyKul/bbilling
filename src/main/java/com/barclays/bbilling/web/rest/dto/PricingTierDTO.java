package com.barclays.bbilling.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the PricingTier entity.
 */
public class PricingTierDTO implements Serializable {

    private Long id;

    @NotNull
    private Long pricingTierFrom;

    @NotNull
    private Long pricingTierTo;

    @NotNull
    private BigDecimal pricingTierValue;

    private Long pricingId;
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

    public Long getPricingId() {
        return pricingId;
    }

    public void setPricingId(Long pricingId) {
        this.pricingId = pricingId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PricingTierDTO pricingTierDTO = (PricingTierDTO) o;

        if ( ! Objects.equals(id, pricingTierDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PricingTierDTO{" +
            "id=" + id +
            ", pricingTierFrom='" + pricingTierFrom + "'" +
            ", pricingTierTo='" + pricingTierTo + "'" +
            ", pricingTierValue='" + pricingTierValue + "'" +
            '}';
    }
}
