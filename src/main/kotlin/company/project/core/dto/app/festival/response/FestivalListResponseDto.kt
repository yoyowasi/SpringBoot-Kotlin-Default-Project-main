package company.project.core.dto.app.festival.response

import java.time.LocalDate

data class FestivalListResponseDto (
	val id: Long,
	val festivalName: String?,
	val holdPlace: String?,
	val festivalStartDate: LocalDate?,
	val festivalEndDate: LocalDate?,
	val rawContent: String?,
	val operatorInstitution: String?,
	val hostInstitution: String?,
	val sponsorInstitution: String?,
	val tel: String?,
	val homepageUrl: String?,
	val relatedInfo: String?,
	val roadAddress: String?,
	val landAddress: String?,
	var latitude: Double?,
	var longitude: Double?,
	val dataStandardDate: LocalDate?,
	val providerInsttCode: String?,
	val providerInsttName: String?,
	val image: String?,
	val like: Boolean? = false,
	val likeCount: Long = 0,
	val category: List<String?>?,
)
