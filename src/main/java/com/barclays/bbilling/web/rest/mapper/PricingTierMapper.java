package com.barclays.bbilling.web.rest.mapper;

import com.barclays.bbilling.domain.*;
import com.barclays.bbilling.web.rest.dto.PricingTierDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PricingTier and its DTO PricingTierDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PricingTierMapper {

    @Mapping(source = "pricing.id", target = "pricingId")
    PricingTierDTO pricingTierToPricingTierDTO(PricingTier pricingTier);

    @Mapping(source = "pricingId", target = "pricing")
    PricingTier pricingTierDTOToPricingTier(PricingTierDTO pricingTierDTO);

    default Pricing pricingFromId(Long id) {
        if (id == null) {
            return null;
        }
        Pricing pricing = new Pricing();
        pricing.setId(id);
        return pricing;
    }
}
