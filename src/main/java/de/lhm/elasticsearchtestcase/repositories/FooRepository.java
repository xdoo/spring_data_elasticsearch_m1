package de.lhm.elasticsearchtestcase.repositories;

import de.lhm.elasticsearchtestcase.model.Foo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface FooRepository extends ElasticsearchRepository<Foo, String> {

}
