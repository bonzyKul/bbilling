package com.barclays.bbilling.repository.search;

import com.barclays.bbilling.domain.Products;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Products entity.
 */
public interface ProductsSearchRepository extends ElasticsearchRepository<Products, Long> {
}
