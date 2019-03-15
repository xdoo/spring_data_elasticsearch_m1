package de.lhm.elasticsearchtestcase.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "foos", type = "foo")
public class Foo {
    @Id
    String id;
    @Field(type = FieldType.Keyword)
    String myfoo;
    @Field(type = FieldType.Nested, includeInParent = true)
    Bar bar;
}
