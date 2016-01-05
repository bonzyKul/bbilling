package com.barclays.bbilling.repository.search;

import com.barclays.bbilling.domain.Pricing;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Pricing entity.
 */
public interface PricingSearchRepository extends ElasticsearchRepository<Pricing, Long> {
}
