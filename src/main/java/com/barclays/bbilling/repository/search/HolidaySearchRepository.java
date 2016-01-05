package com.barclays.bbilling.repository.search;

import com.barclays.bbilling.domain.Holiday;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Holiday entity.
 */
public interface HolidaySearchRepository extends ElasticsearchRepository<Holiday, Long> {
}
