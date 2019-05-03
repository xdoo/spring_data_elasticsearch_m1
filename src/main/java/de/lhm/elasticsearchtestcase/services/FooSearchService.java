package de.lhm.elasticsearchtestcase.services;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import de.lhm.elasticsearchtestcase.model.Foo;
import de.lhm.elasticsearchtestcase.repositories.FooRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FooSearchService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final FooRepository fooRepository;

    public final static String FOO_SUGGEST = "foo-suggest";

    public FooSearchService(ElasticsearchOperations elasticsearchOperations, FooRepository fooRepository) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.fooRepository = fooRepository;
    }

    /**
     * Search for query
     *
     * @param query
     * @param page
     * @return
     */
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
        return foos;
    }

    public Page<Foo> searchForFilterFoos(String query, String myfooFilter, String mybarFilter, int page) {
        QueryStringQueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(query);
        queryStringQueryBuilder.field("title");
        queryStringQueryBuilder.field("bar.mybar");

        Lists.newArrayList("bar.mybar", "myfoo");

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(queryStringQueryBuilder)
//                .filter(termQuery("bar.mybar", mybarFilter))
//                .filter(termQuery("myfoo", myfooFilter))
                ;
        if(!Strings.isNullOrEmpty(mybarFilter)) {
            boolQueryBuilder.filter(termQuery("bar.mybar", mybarFilter));
        }

        if(!Strings.isNullOrEmpty(myfooFilter)) {
            boolQueryBuilder.filter(termQuery("myfoo", myfooFilter));
        }

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("foos")
                .withQuery(boolQueryBuilder)
                .build();

//        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withIndices("foos")
//                .withQuery(queryStringQueryBuilder)
//                .withFilter(termQuery("bar.mybar", mybarFilter))
//                .withFilter(termQuery("myfoo", myfooFilter))
//                .withPageable(PageRequest.of(page, 15))
//                .build();

        Page<Foo> foos = this.fooRepository.search(searchQuery);
        return foos;
    }

    /**
     * Suggest for autocomplete
     *
     * @param query
     */
    public List<String> suggestForFoo(String query) {
        CompletionSuggestionBuilder suggest = SuggestBuilders.completionSuggestion("suggest").prefix(query, Fuzziness.TWO).skipDuplicates(true).size(5);
        ElasticsearchRestTemplate template = (ElasticsearchRestTemplate)this.elasticsearchOperations;
        SearchResponse searchResponse = template.suggest(new SuggestBuilder().addSuggestion(FOO_SUGGEST, suggest), Foo.class);

        // extract text
        List<? extends Suggest.Suggestion.Entry.Option> options = searchResponse.getSuggest().getSuggestion(FOO_SUGGEST).getEntries().get(0).getOptions();
        List<String> result = new ArrayList<>();
        options.forEach(o -> {
           result.add(o.getText().string());
        });

        return result;
    }
}
