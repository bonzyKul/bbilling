package com.barclays.bbilling.web.rest.mapper;

import com.barclays.bbilling.domain.*;
import com.barclays.bbilling.web.rest.dto.BranchDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Branch and its DTO BranchDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BranchMapper {

    BranchDTO branchToBranchDTO(Branch branch);

    Branch branchDTOToBranch(BranchDTO branchDTO);
}
