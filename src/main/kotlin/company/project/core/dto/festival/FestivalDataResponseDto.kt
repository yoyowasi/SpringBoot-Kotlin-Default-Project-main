package company.project.core.dto.festival

import com.fasterxml.jackson.annotation.JsonProperty

data class FestivalDataResponseDto(
	val fields: List<FestivalFieldDto>,
	val records: List<FestivalRecordDto>,
)
data class FestivalFieldDto(
	val id: String,
)
data class FestivalRecordDto(
	@JsonProperty("축제명")
	val fstvlNm: String?,
	@JsonProperty("개최장소")
	val holdPlace: String?,
	@JsonProperty("축제시작일자")
	val fstvlStartDate: String?,
	@JsonProperty("축제종료일자")
	val fstvlEndDate: String?,
	@JsonProperty("축제내용")
	val fstvlCo: String?,
	@JsonProperty("주관기관명")
	val oprtInstitNm: String?,
	@JsonProperty("주최기관명")
	val hostInstitNm: String?,
	@JsonProperty("후원기관명")
	val sponInstitNm: String?,
	@JsonProperty("전화번호")
	val telno: String?,
	@JsonProperty("홈페이지주소")
	val homepageUrl: String?,
	@JsonProperty("관련정보")
	val relInfo: String?,
	@JsonProperty("소재지도로명주소")
	val rdnmadr: String?,
	@JsonProperty("소재지지번주소")
	val lnmadr: String?,
	@JsonProperty("위도")
	var latitude: String?,
	@JsonProperty("경도")
	var longitude: String?,
	@JsonProperty("데이터기준일자")
	val dataStdDe: String?,
	@JsonProperty("제공기관코드")
	val providerInsttCode: String?,
	@JsonProperty("제공기관명")
	val providerInsttNm: String?,
)
