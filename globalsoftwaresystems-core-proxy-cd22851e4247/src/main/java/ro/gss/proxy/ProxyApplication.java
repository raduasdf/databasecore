package ro.gss.proxy;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableZuulProxy
public class ProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (certificate, authType) -> true)
				.build();
		CloseableHttpClient httpClient = HttpClients.custom().setSSLContext(sslContext)
				.setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setHttpClient(httpClient);
		return new RestTemplate(requestFactory);
	}
}