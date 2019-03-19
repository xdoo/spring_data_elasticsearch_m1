package de.lhm.elasticsearchtestcase;

import de.lhm.elasticsearchtestcase.model.Bar;
import de.lhm.elasticsearchtestcase.model.Foo;
import de.lhm.elasticsearchtestcase.repositories.FooRepository;
import de.lhm.elasticsearchtestcase.services.FooSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.suggest.Suggest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.completion.Completion;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class FooSearchServiceSpringTests {

    @Autowired
    FooSearchService fooSearchService;

    @Autowired
    FooRepository fooRepository;

	@Test
	public void testSearchForFoo() {

        this.createEntries();

        // query for my foo
        Page<Foo> foos01 = this.fooSearchService.searchForFoo("xyz*", 0);
        assertThat(foos01.getTotalElements(), is(equalTo(2L)));
        this.logFoos(foos01);

        // query for an other foo
        Page<Foo> foos02 = this.fooSearchService.searchForFoo("poiu*", 0);
        assertThat(foos02.getTotalElements(), is(equalTo(2L)));

        // query for a bar attribute
        Page<Foo> foos03 = this.fooSearchService.searchForFoo("asdf*", 0);
        assertThat(foos03.getTotalElements(), is(equalTo(2L)));

        // query for a bar and foo attribute
        Page<Foo> foos04 = this.fooSearchService.searchForFoo("asdf* poiu*", 0);
        assertThat(foos04.getTotalElements(), is(equalTo(3L)));

    }

    @Test
    public void testSuggestForFoo() {

	    this.createEntries();

        List<String> suggests = this.fooSearchService.suggestForFoo("xyzf");
        assertThat(suggests.size(), is(2));
        assertThat(suggests.get(0), isOneOf("xyzfoo", "xyztitle"));
        assertThat(suggests.get(1), isOneOf("xyzfoo", "xyztitle"));
    }

    private void logFoos(Page<Foo> foos ) {
	    foos.get().forEach(foo -> {
	        log.info("foo -> {}", foo.toString());
        });
    }

    private void createEntries() {

	    // clear index
        this.fooRepository.deleteAll();

        // save 4 different foo objects to index 'foos'

        // 01
        Foo foo01 = new Foo(
                "FOO0001",
                "xyzfoo",
                "awesome title",
                new Bar("qwerbar")
        );
        Completion c001 = new Completion(new String[]{"xyzfoo", "awesome", "title", "FOO0001", "xyzfoo awesome"});
        foo01.setSuggest(c001);
        this.fooRepository.save(foo01);

        // 02
        Foo foo02 =new Foo(
                "FOO0002",
                "xyzfoo",
                "poiu title",
                new Bar("asdfbar")
        );
        Completion c002 = new Completion(new String[]{"FOO0002", "xyzfoo", "poiu", "title", "poiu title"});
        foo02.setSuggest(c002);
        this.fooRepository.save(foo02);

        // 03
        Foo foo03 =new Foo(
                "FOO0003",
                "poiufoo",
                "xyztitle",
                new Bar("qwerbar")
        );
        Completion c003 = new Completion(new String[]{"FOO0003", "poiufoo", "xyztitle"});
        foo03.setSuggest(c003);
        this.fooRepository.save(foo03);

        // 04
        Foo foo04 =new Foo(
                "FOO0004",
                "poiufoo",
                "Boobel dibubb",
                new Bar("asdfbar")
        );
        Completion c004 = new Completion(new String[]{"FOO0004", "poiufoo", "Boobel", "dibubb", "Boobel dibubb"});
        foo04.setSuggest(c004);
        this.fooRepository.save(foo04);

        // check save
        assertThat(this.fooRepository.count(), is(equalTo(4L)));
    }

}
