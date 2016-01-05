package com.barclays.bbilling.web.rest.mapper;

import com.barclays.bbilling.domain.*;
import com.barclays.bbilling.web.rest.dto.ProductsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Products and its DTO ProductsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProductsMapper {

    @Mapping(source = "productFamily.id", target = "productFamilyId")
    @Mapping(source = "productFamily.productFamilyDesc", target = "productFamilyProductFamilyDesc")
    ProductsDTO productsToProductsDTO(Products products);

    @Mapping(source = "productFamilyId", target = "productFamily")
    @Mapping(target = "serviceCatalogues", ignore = true)
    Products productsDTOToProducts(ProductsDTO productsDTO);

    default ProductFamily productFamilyFromId(Long id) {
        if (id == null) {
            return null;
        }
        ProductFamily productFamily = new ProductFamily();
        productFamily.setId(id);
        return productFamily;
    }
}
