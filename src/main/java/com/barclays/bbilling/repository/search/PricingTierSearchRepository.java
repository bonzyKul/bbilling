package com.barclays.bbilling.repository.search;

import com.barclays.bbilling.domain.PricingTier;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PricingTier entity.
 */
public interface PricingTierSearchRepository extends ElasticsearchRepository<PricingTier, Long> {
}
