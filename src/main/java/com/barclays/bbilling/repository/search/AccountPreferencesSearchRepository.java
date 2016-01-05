package com.barclays.bbilling.repository.search;

import com.barclays.bbilling.domain.AccountPreferences;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AccountPreferences entity.
 */
public interface AccountPreferencesSearchRepository extends ElasticsearchRepository<AccountPreferences, Long> {
}
