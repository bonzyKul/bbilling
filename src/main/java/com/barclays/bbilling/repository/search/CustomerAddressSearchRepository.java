package com.barclays.bbilling.repository.search;

import com.barclays.bbilling.domain.CustomerAddress;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CustomerAddress entity.
 */
public interface CustomerAddressSearchRepository extends ElasticsearchRepository<CustomerAddress, Long> {
}
