package company.project.core.dto.app.festival

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class FestivalListResponseDto (
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
	val category: List<String?>,
)
