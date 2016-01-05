package com.barclays.bbilling.repository.search;

import com.barclays.bbilling.domain.AccountAppHLD;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AccountAppHLD entity.
 */
public interface AccountAppHLDSearchRepository extends ElasticsearchRepository<AccountAppHLD, Long> {
}
