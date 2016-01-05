package com.barclays.bbilling.web.rest.mapper;

import com.barclays.bbilling.domain.*;
import com.barclays.bbilling.web.rest.dto.ProductFamilyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProductFamily and its DTO ProductFamilyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProductFamilyMapper {

    ProductFamilyDTO productFamilyToProductFamilyDTO(ProductFamily productFamily);

    @Mapping(target = "productss", ignore = true)
    ProductFamily productFamilyDTOToProductFamily(ProductFamilyDTO productFamilyDTO);
}
