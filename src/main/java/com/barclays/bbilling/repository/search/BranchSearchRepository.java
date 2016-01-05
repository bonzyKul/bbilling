package com.barclays.bbilling.repository.search;

import com.barclays.bbilling.domain.Branch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Branch entity.
 */
public interface BranchSearchRepository extends ElasticsearchRepository<Branch, Long> {
}
