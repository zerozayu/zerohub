package com.zerohub.web.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

/**
 * es 客户端初始化
 *
 * @author zhangyu
 * @date 2022/9/27 15:45
 */
@Configuration
@Slf4j
public class ESClient {

    @Value("${elasticsearch.host}")
    private String host;
    @Value("${elasticsearch.port}")
    private Integer port;
    @Value("${elasticsearch.scheme}")
    private String scheme;
    @Value("${elasticsearch.username}")
    private String username;
    @Value("${elasticsearch.password}")
    private String password;
    @Value("${elasticsearch.caCertificatePath}")
    private String path;
    private ElasticsearchClient esClient;

    @PostConstruct
    private void initClient() {

        RestClientBuilder restClientBuilder = RestClient.builder(new HttpHost(host, port, scheme));
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        // 设置证书以及用户认证信息
        setCaCertificate(restClientBuilder, credentialsProvider);

        // 构建 RestClientTransport
        ObjectMapper objectMapper = new ObjectMapper();

        RestClientTransport restClientTransport = new RestClientTransport(restClientBuilder.build(), new JacksonJsonpMapper(objectMapper));

        // RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        // RequestOptions requestOptions = builder.build();
        // TransportOptions transportOptions = new RestClientOptions(requestOptions);

        // 实例化 ElasticsearchClient
        esClient = new ElasticsearchClient(restClientTransport);

    }

    /**
     * 设置 es 连接所需的证书以及用户名密码
     * https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/7.17/_encrypted_communication.html
     *
     * @param restClientBuilder   es客户端
     * @param credentialsProvider 用户认证信息
     */
    private void setCaCertificate(RestClientBuilder restClientBuilder, CredentialsProvider credentialsProvider) {
        // 数字证书
        Path caCertificatePath = Paths.get(path);
        try {
            // X.509是密码学里公钥证书的格式标准。X.509 证书里含有公钥、身份信息（比如网络主机名，组织的名称或个体名称等）和签名信息（可以是证书签发机构CA的签名，也可以是自签名）。
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            Certificate trustedCa;
            try (InputStream is = Files.newInputStream(caCertificatePath)) {
                trustedCa = factory.generateCertificate(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // 证书库
            KeyStore trustStore = KeyStore.getInstance("pkcs12");
            // 初始化空证书库
            trustStore.load(null, null);
            trustStore.setCertificateEntry("ca", trustedCa);
            SSLContextBuilder sslContextBuilder = SSLContexts.custom().loadTrustMaterial(trustStore, null);
            final SSLContext sslContext = sslContextBuilder.build();

            // 设置证书以及用户名密码
            restClientBuilder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                    // 设置 ssl
                    .setSSLContext(sslContext)
                    // 设置用户认证信息
                    .setDefaultCredentialsProvider(credentialsProvider));

        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException |
                 KeyManagementException e) {
            log.error("设置 ssl 以及用户认证信息->{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        return this.esClient;
    }

}
