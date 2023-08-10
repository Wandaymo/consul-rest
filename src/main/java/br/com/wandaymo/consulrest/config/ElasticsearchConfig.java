package br.com.wandaymo.consulrest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    @Value("${elastic.client.username}")
    private String elasticUserName;

    @Value("${elastic.client.password}")
    private String elasticPassword;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder().connectedTo("localhost:9200")
                .usingSsl("c32dd52872ab29849c6a9e5a821f0c70306771dd27f4fe224fc86bb60ebbc03d")
                .withBasicAuth(elasticUserName, elasticPassword).build();
    }
}
