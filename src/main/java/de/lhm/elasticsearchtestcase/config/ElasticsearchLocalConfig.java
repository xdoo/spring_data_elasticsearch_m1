package de.lhm.elasticsearchtestcase.config;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "de/lhm/elasticsearchtestcase/repositories")
@Slf4j
public class ElasticsearchLocalConfig {

    @Value(value = "${elasticsearch.host:localhost}")
    private String host;

    @Value(value = "${elasticsearch.port:9200}")
    private int port;

    @Bean
    RestHighLevelClient client() {

        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(this.host+":"+this.port)
                .build();

        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate(RestHighLevelClient client) {
        return new ElasticsearchRestTemplate(client);
    }

}
