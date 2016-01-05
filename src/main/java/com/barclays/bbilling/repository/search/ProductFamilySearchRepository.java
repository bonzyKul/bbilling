package com.barclays.bbilling.repository.search;

import com.barclays.bbilling.domain.ProductFamily;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ProductFamily entity.
 */
public interface ProductFamilySearchRepository extends ElasticsearchRepository<ProductFamily, Long> {
}
