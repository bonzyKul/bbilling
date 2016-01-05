package com.barclays.bbilling.web.rest.mapper;

import com.barclays.bbilling.domain.*;
import com.barclays.bbilling.web.rest.dto.ServiceCatalogueDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ServiceCatalogue and its DTO ServiceCatalogueDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ServiceCatalogueMapper {

    @Mapping(source = "products.id", target = "productsId")
    @Mapping(source = "products.productName", target = "productsProductName")
    ServiceCatalogueDTO serviceCatalogueToServiceCatalogueDTO(ServiceCatalogue serviceCatalogue);

    @Mapping(source = "productsId", target = "products")
    @Mapping(target = "pricings", ignore = true)
    ServiceCatalogue serviceCatalogueDTOToServiceCatalogue(ServiceCatalogueDTO serviceCatalogueDTO);

    default Products productsFromId(Long id) {
        if (id == null) {
            return null;
        }
        Products products = new Products();
        products.setId(id);
        return products;
    }
}
