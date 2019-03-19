package de.lhm.elasticsearchtestcase.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.completion.Completion;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Document(indexName = "foos", type = "foo")
public class Foo {
    @Id @NonNull
    String id;

    @NonNull
    @Field(type = FieldType.Text)
    String myfoo;

    @NonNull
    @Field(type = FieldType.Text)
    String title;

    @NonNull
    @Field(type = FieldType.Nested, includeInParent = true)
    Bar bar;

    @CompletionField
    Completion suggest;
}
