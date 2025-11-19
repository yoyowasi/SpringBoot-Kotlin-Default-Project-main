package company.project.infra.config

import org.apache.hc.client5.http.classic.HttpClient
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder
import org.apache.hc.client5.http.ssl.TrustAllStrategy
import org.apache.hc.core5.ssl.SSLContextBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import javax.net.ssl.SSLContext
import org.springframework.context.annotation.Primary

@Configuration
class HttpConfig {

	@Bean
	@Primary  // 이 빈을 기본으로 사용하도록 설정
	fun restTemplate(): RestTemplate {
		val sslContext: SSLContext = SSLContextBuilder()
			.loadTrustMaterial(null, TrustAllStrategy.INSTANCE)
			.build()

		val sslSocketFactory: SSLConnectionSocketFactory = SSLConnectionSocketFactoryBuilder
			.create()
			.setSslContext(sslContext)
			.build()

		val connectionManager = PoolingHttpClientConnectionManagerBuilder
			.create()
			.setSSLSocketFactory(sslSocketFactory)
			.build()

		val httpClient: HttpClient = HttpClients.custom()
			.setConnectionManager(connectionManager)
			.build()

		val requestFactory = HttpComponentsClientHttpRequestFactory()
		requestFactory.httpClient = httpClient

		return RestTemplate(requestFactory)
	}
}
