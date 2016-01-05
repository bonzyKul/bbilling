package com.barclays.bbilling.repository.search;

import com.barclays.bbilling.domain.Currency;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Currency entity.
 */
public interface CurrencySearchRepository extends ElasticsearchRepository<Currency, Long> {
}
