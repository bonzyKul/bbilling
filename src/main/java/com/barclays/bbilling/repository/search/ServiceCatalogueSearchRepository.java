package com.barclays.bbilling.repository.search;

import com.barclays.bbilling.domain.ServiceCatalogue;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ServiceCatalogue entity.
 */
public interface ServiceCatalogueSearchRepository extends ElasticsearchRepository<ServiceCatalogue, Long> {
}
