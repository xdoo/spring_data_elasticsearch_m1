package de.lhm.elasticsearchtestcase;

import de.lhm.elasticsearchtestcase.util.SearchUtil;
import org.junit.Test;

public class SearchUtilTests {

    @Test
    public void testCreateCompletion() {
        SearchUtil.createCompletion("Hans", "Tester", "Sendling", "80995", "Haupstraße", "FOO");
    }

}
