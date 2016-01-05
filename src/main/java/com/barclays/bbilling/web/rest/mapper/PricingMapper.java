package com.barclays.bbilling.web.rest.mapper;

import com.barclays.bbilling.domain.*;
import com.barclays.bbilling.web.rest.dto.PricingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Pricing and its DTO PricingDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PricingMapper {

    @Mapping(source = "products.id", target = "productsId")
    @Mapping(source = "products.productCode", target = "productsProductCode")
    @Mapping(source = "currency.id", target = "currencyId")
    @Mapping(source = "currency.currencyCode", target = "currencyCurrencyCode")
    @Mapping(source = "serviceCatalogue.id", target = "serviceCatalogueId")
    @Mapping(source = "serviceCatalogue.serviceCode", target = "serviceCatalogueServiceCode")
    PricingDTO pricingToPricingDTO(Pricing pricing);

    @Mapping(source = "productsId", target = "products")
    @Mapping(source = "currencyId", target = "currency")
    @Mapping(target = "pricingTiers", ignore = true)
    @Mapping(source = "serviceCatalogueId", target = "serviceCatalogue")
    Pricing pricingDTOToPricing(PricingDTO pricingDTO);

    default Products productsFromId(Long id) {
        if (id == null) {
            return null;
        }
        Products products = new Products();
        products.setId(id);
        return products;
    }

    default Currency currencyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Currency currency = new Currency();
        currency.setId(id);
        return currency;
    }

    default ServiceCatalogue serviceCatalogueFromId(Long id) {
        if (id == null) {
            return null;
        }
        ServiceCatalogue serviceCatalogue = new ServiceCatalogue();
        serviceCatalogue.setId(id);
        return serviceCatalogue;
    }
}
