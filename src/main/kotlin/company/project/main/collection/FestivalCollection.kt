package company.project.main.collection

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import company.project.core.dto.festival.FestivalDataResponseDto
import company.project.lib.common.util.Http
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class FestivalCollection(
	private val http : Http
) {
	@Value("\${api.key}")
	private lateinit var key: String

	private val endpoint = "http://api.data.go.kr/openapi/tn_pubr_public_cltur_fstvl_api"
	private val objectMapper: ObjectMapper = ObjectMapper().registerKotlinModule()

	fun request(page: Int): String? {
		val builder = UriComponentsBuilder.fromHttpUrl(endpoint)
			.queryParam("serviceKey", key)
			.queryParam("type", "json")
			.queryParam("pageNo", page)
			.queryParam("numOfRows", 2)

		println("raw: ${builder.toUriString()}")
		println("encoded: ${builder.encode().toUriString()}")

		val url = builder.encode().toUriString()

		val headers = mapOf(
			"Accept" to "application/json",
			"User-Agent" to "Mozilla/5.0"
		)
		return http.get(url, headers, String::class.java)
	}

	fun getJsonData(): FestivalDataResponseDto {
		// classpath: data/json/festival.json
		val resource = ClassPathResource("data/json/festival.json")
		resource.inputStream.use { input ->
			// JSON 문자열을 DTO로 변환
			return objectMapper.readValue(input)
		}
	}
}
