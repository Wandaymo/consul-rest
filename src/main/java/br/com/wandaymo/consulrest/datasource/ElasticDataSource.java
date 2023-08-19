package br.com.wandaymo.consulrest.datasource;

import br.com.wandaymo.consulrest.entity.Person;
import br.com.wandaymo.consulrest.entity.Restrictive;
import br.com.wandaymo.consulrest.util.RandomGenerate;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientOptions;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Base64;
import javax.net.ssl.SSLContext;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.transport.http.HttpTransportConstants;

@Component
public class ElasticDataSource {

    @Value("${http.server.ssl.trust-store}")
    private Resource trustStore;

    @Value("${http.server.ssl.trust-store-password}")
    private String trustStorePassword;

    @Value("${elastic.client.username}")
    private String elasticUserName;

    @Value("${elastic.client.password}")
    private String elasticPassword;

    @Value("${elastic.host}")
    private String elasticHost;

    @Value("${elastic.port}")
    private String elasticPort;

    @Autowired
    private RandomGenerate randomGenerate;

    @Autowired
    @Qualifier("restTemplateElasticsearch")
    private RestTemplate restTemplate;

    private static final Logger LOG = LoggerFactory.getLogger(ElasticDataSource.class);

    @Autowired
    private ElasticsearchTransport transport;

    //@EventListener(ApplicationReadyEvent.class)
    public void populateData() throws CertificateException, IOException, NoSuchAlgorithmException, KeyStoreException,
            KeyManagementException {

        //delete("restrictive");
        //delete("person");

        LOG.info("START: {}", DateTime.now().toLocalTime());
        var i = 0;
        var j = 0;
        for (j = 0; j < 6; j++) {
            Person person = randomGenerate.generatePerson();
            insertData(person, person.getId(), "person");
            for (i = 0; i < 16; i++) {
                if (i == 1) {
                    LOG.info(person.getName());
                    LOG.info(person.getCpfCnpj());
                }

                Restrictive restrictive = randomGenerate.generateRestrictive();
                restrictive.setPersonId(person.getId());
                insertData(restrictive, restrictive.getId(), "restrictive");

            }
            LOG.info("Iteração {}", j);
        }

        transport.close();

        LOG.info("END: {}", DateTime.now().toLocalTime());
        LOG.info("Saved {} persons", j);
        LOG.info("Saved {} restrictives", i * j);
    }

    private void insertData(Object object, String id, String indexName) throws IOException,
            CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray()).build();

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(elasticUserName, elasticPassword));

        RestClient httpClient = RestClient.builder(new HttpHost(elasticHost, Integer.parseInt(elasticPort),
                        HttpTransportConstants.HTTPS_URI_SCHEME)).setHttpClientConfigCallback(
                        httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                                .setSSLContext(sslContext))
                .setDefaultHeaders(new Header[]{new BasicHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)})
                .build();

        transport = new RestClientTransport(httpClient, new JacksonJsonpMapper())
                .withRequestOptions(new RestClientOptions.Builder(RequestOptions.DEFAULT.toBuilder()
                        .addHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)).build());

        ElasticsearchClient client = new ElasticsearchClient(transport);
        BulkRequest.Builder builder = new BulkRequest.Builder();

        builder.operations(op -> op
                .index(idx -> idx
                        .index(indexName)
                        .id(id)
                        .document(object)
                )
        );

        BulkResponse bulkResponse = client.bulk(builder.build());

        if (bulkResponse.errors()) {
            LOG.error("Bulk had errors");
            for (BulkResponseItem item : bulkResponse.items()) {
                if (item.error() != null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.error("An error occurred: {} ", item.error().reason());
                    }
                    throw new IOException("An error occurred: " + item.error().reason());
                }
            }
        }
    }

    private void delete(String index) {
        var url = "https://localhost:9200/" + index;
        var headers = new HttpHeaders();
        var authCredential = elasticUserName + ":" + elasticPassword;
        var authHeader = "Basic " + Base64.getEncoder().encodeToString(authCredential.getBytes());
        headers.set(HttpHeaders.AUTHORIZATION, authHeader);

        var entity = new HttpEntity<String>(headers);
        var response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);

        LOG.info("Response: {}", response.getBody());
    }
}
