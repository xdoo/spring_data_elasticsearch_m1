package de.lhm.elasticsearchtestcase;

import de.lhm.elasticsearchtestcase.model.Bar;
import de.lhm.elasticsearchtestcase.model.Foo;
import de.lhm.elasticsearchtestcase.repositories.FooRepository;
import de.lhm.elasticsearchtestcase.services.FooSearchService;
import org.apache.commons.lang3.RandomStringUtils;
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
public class FooSearchServiceSpringTests {

    @Autowired
    FooSearchService fooSearchService;

    @Autowired
    FooRepository fooRepository;

	@Test
	public void testSearchForFoo() {

	    // save 4 different foo objects to index 'foos'
        Foo foo01 = new Foo(
                RandomStringUtils.randomAlphanumeric(35).toUpperCase(),
                "xyzfoo",
                new Bar("qwerbar")
        );
        this.fooRepository.save(foo01);
        Foo foo02 =new Foo(
                RandomStringUtils.randomAlphanumeric(35).toUpperCase(),
                "xyzfoo",
                new Bar("asdfbar")
        );
        this.fooRepository.save(foo02);
        Foo foo03 =new Foo(
                RandomStringUtils.randomAlphanumeric(35).toUpperCase(),
                "poiufoo",
                new Bar("qwerbar")
        );
        this.fooRepository.save(foo03);
        Foo foo04 =new Foo(
                RandomStringUtils.randomAlphanumeric(35).toUpperCase(),
                "poiufoo",
                new Bar("asdfbar")
        );
        this.fooRepository.save(foo04);

        // check save
        assertThat(this.fooRepository.count(), is(equalTo(4)));

        // query for my foo
        Page<Foo> foos01 = this.fooSearchService.searchForFoo("xyz*", 0);
        assertThat(foos01.getTotalElements(), is(equalTo(2)));

    }

}
