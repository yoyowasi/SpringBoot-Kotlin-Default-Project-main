package company.project.main.collection

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import company.project.core.dto.festival.BingImageResponse
import company.project.core.dto.festival.FestivalDataResponseDto
import company.project.core.dto.festival.FestivalRecordDto
import company.project.core.dto.festival.KakaoAddressSearchResponse
import company.project.lib.common.util.Http
import java.net.URL
import java.net.URLDecoder
import kotlin.text.get
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
	@Value("\${bing.key}")
	private lateinit var bingKey: String


	private val endpoint = "http://api.data.go.kr/openapi/tn_pubr_public_cltur_fstvl_api"
	private val locateEndpoint = "https://dapi.kakao.com/v2/local/search/address.json?query="
	private val objectMapper: ObjectMapper = ObjectMapper().registerKotlinModule()
	private val bingEndpoint = "https://api.bing.microsoft.com/v7.0/images/search"




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

	fun searchFestivalImage(festivalName: String): BingImageResponse? {
		val uri = UriComponentsBuilder.fromHttpUrl(bingEndpoint)
			.queryParam("q", "$festivalName")
			.queryParam("count", 1)   // 1장만 가져오기
			.build()
			.toString()


		val headers = mapOf(
			"Ocp-Apim-Subscription-Key" to bingKey)

		return http.get(uri, headers, BingImageResponse::class.java)
	}
	fun getJsonData(): FestivalDataResponseDto {
		// classpath: data/json/festival.json
		val resource = ClassPathResource("data/json/festival.json")
		resource.inputStream.use { input ->
			// JSON 문자열을 DTO로 변환
			return objectMapper.readValue(input)
		}
	}

	fun getLatitudeLongtide(record: FestivalRecordDto): KakaoAddressSearchResponse? {
		val address = URLDecoder.decode(if(record.rdnmadr != "") record.rdnmadr else record.lnmadr , "UTF-8")
		println("address: $address")
		val url = "$locateEndpoint$address"
		val headers = mapOf(
			"authorization" to "KakaoAK ac759e3deb7f9c8656758bdc177f8151"
		)
		println("url: $url")
		return http.get(url, headers, KakaoAddressSearchResponse::class.java)
	}
}
