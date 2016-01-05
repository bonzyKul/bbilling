package com.barclays.bbilling.repository.search;

import com.barclays.bbilling.domain.Bank;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Bank entity.
 */
public interface BankSearchRepository extends ElasticsearchRepository<Bank, Long> {
}
