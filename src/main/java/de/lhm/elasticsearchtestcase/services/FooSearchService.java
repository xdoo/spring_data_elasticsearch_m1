package de.lhm.elasticsearchtestcase.services;

import de.lhm.elasticsearchtestcase.model.Foo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

@Service
@Slf4j
public class FooSearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    public FooSearchService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public Page<Foo> searchForFoo(String query, int page) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("foos")
                .withFields("myfoo", "bar.mybar")
                .withQuery(queryStringQuery(query))
                .withPageable(PageRequest.of(page, 15))
                .build();

        Page<Foo> foos = this.elasticsearchOperations.queryForPage(searchQuery, Foo.class);
        return foos;
    }
}
