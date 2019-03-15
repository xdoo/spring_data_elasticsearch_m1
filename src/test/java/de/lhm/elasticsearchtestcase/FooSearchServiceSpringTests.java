package de.lhm.elasticsearchtestcase;

import de.lhm.elasticsearchtestcase.model.Bar;
import de.lhm.elasticsearchtestcase.model.Foo;
import de.lhm.elasticsearchtestcase.repositories.FooRepository;
import de.lhm.elasticsearchtestcase.services.FooSearchService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

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

	    // save 4 different foo objects to index 'foos'
        Foo foo01 = new Foo(
                "FOO0001",
                "xyzfoo",
                "awesome title",
                new Bar("qwerbar")
        );
        this.fooRepository.save(foo01);
        Foo foo02 =new Foo(
                "FOO0002",
                "xyzfoo",
                "poiu title",
                new Bar("asdfbar")
        );
        this.fooRepository.save(foo02);
        Foo foo03 =new Foo(
                "FOO0003",
                "poiufoo",
                "xyztitle",
                new Bar("qwerbar")
        );
        this.fooRepository.save(foo03);
        Foo foo04 =new Foo(
                "FOO0004",
                "poiufoo",
                null,
                new Bar("asdfbar")
        );
        this.fooRepository.save(foo04);

        // check save
        assertThat(this.fooRepository.count(), is(equalTo(4L)));

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

    private void logFoos(Page<Foo> foos ) {
	    foos.get().forEach(foo -> {
	        log.info("foo -> {}", foo.toString());
        });
    }

}
