package company.project.lib.common.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import java.net.URI
import org.springframework.web.client.*
import kotlin.jvm.java
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable

@Component
class Http (
	private val restTemplate: RestTemplate
){
	private val log = LoggerFactory.getLogger(this::class.java)

	@Retryable(include = [HttpServerErrorException::class], maxAttempts = 3, backoff = Backoff(delay = 2000))
	fun <T> get(
		url: String,
		headers: Map<String, String>,
		responseClass: Class<T>,
	): T? {
		val httpHeaders = HttpHeaders()
		httpHeaders.setAll(headers)

		return try {
			val response = restTemplate.exchange(
				url,
				HttpMethod.GET,
				HttpEntity<Any>(httpHeaders),
				responseClass,
			)
			response.body
		} catch (e: RestClientResponseException) {
			log.warn("HTTP 응답 오류: 상태코드=${e.rawStatusCode}, URL=$url, 응답본문=${e.responseBodyAsString}")
			throw e
		} catch (e: Exception) {
			log.error("예기치 않은 HTTP 요청 에러: $e, URL=$url")
			e.printStackTrace()
			throw e
		}
	}
}
