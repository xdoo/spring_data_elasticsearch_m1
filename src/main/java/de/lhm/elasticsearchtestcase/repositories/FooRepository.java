package de.lhm.elasticsearchtestcase.repositories;

import de.lhm.elasticsearchtestcase.model.Foo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface FooRepository extends ElasticsearchRepository<Foo, String> {
}
