package com.barclays.bbilling.repository.search;

import com.barclays.bbilling.domain.AccountFamily;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the AccountFamily entity.
 */
public interface AccountFamilySearchRepository extends ElasticsearchRepository<AccountFamily, Long> {
}
