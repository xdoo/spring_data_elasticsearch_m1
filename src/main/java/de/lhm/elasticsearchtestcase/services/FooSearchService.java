package de.lhm.elasticsearchtestcase.services;

import de.lhm.elasticsearchtestcase.model.Foo;
import de.lhm.elasticsearchtestcase.repositories.FooRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
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
    private final FooRepository fooRepository;

    public FooSearchService(ElasticsearchOperations elasticsearchOperations, FooRepository fooRepository) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.fooRepository = fooRepository;
    }

    public Page<Foo> searchForFoo(String query, int page) {

        QueryStringQueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(query);
        queryStringQueryBuilder.field("myfoo");
        queryStringQueryBuilder.field("bar.mybar");


        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("foos")
                .withQuery(queryStringQueryBuilder)
                .withPageable(PageRequest.of(page, 15))
                .build();

        Page<Foo> foos = this.fooRepository.search(searchQuery);
//        Page<Foo> foos = this.elasticsearchOperations.queryForPage(searchQuery, Foo.class);
        return foos;
    }
}
