package de.lhm.elasticsearchtestcase.util;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.processing.Completion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class SearchUtil {

    public static void createCompletion(String ...fields) {
        // single values
        List<String> input = Lists.newArrayList(fields);

        List<List<String>> values = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            values.add(Lists.newArrayList(fields[i]));
        }

        //
        int cnt = values.size();
        for(int i = 0; i < cnt; i++ ) {
            List<List<String>> lists = Lists.cartesianProduct(values);
            input.add(String.join(" ", lists.get(0)));
            if(values.size() > 0) {
                values.remove(0);
            }
        }

        // log
        input.forEach(x -> {
            log.info(x);
        });
    }
}
