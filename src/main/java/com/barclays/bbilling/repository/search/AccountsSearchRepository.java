package com.barclays.bbilling.repository.search;

import com.barclays.bbilling.domain.Accounts;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Accounts entity.
 */
public interface AccountsSearchRepository extends ElasticsearchRepository<Accounts, Long> {
}
