package company.project.core.entity

import company.project.core.dto.app.festival.response.FestivalListResponseDto
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate

@Entity
@Table(name = "festival", schema = "project")
open class Festival {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	open var id: Long? = null

	@Size(max = 255)
	@NotNull
	@Column(name = "festival_name", nullable = false)
	open var festivalName: String? = null

	@Size(max = 255)
	@Column(name = "hold_place")
	open var holdPlace: String? = null

	@Column(name = "festival_start_date")
	open var festivalStartDate: LocalDate? = null

	@Column(name = "festival_end_date")
	open var festivalEndDate: LocalDate? = null

	@Size(max = 500)
	@Column(name = "raw_content")
	open var rawContent: String? = null

	@Size(max = 255)
	@Column(name = "operator_institution")
	open var operatorInstitution: String? = null

	@Size(max = 255)
	@Column(name = "host_institution")
	open var hostInstitution: String? = null

	@Size(max = 255)
	@Column(name = "sponsor_institution")
	open var sponsorInstitution: String? = null

	@Size(max = 50)
	@Column(name = "tel", length = 50)
	open var tel: String? = null

	@Size(max = 500)
	@Column(name = "homepage_url", length = 500)
	open var homepageUrl: String? = null

	@Size(max = 255)
	@Column(name = "related_info")
	open var relatedInfo: String? = null

	@Size(max = 500)
	@Column(name = "road_address", length = 500)
	open var roadAddress: String? = null

	@Size(max = 500)
	@Column(name = "land_address", length = 500)
	open var landAddress: String? = null

	@Column(name = "latitude")
	open var latitude: Double? = null

	@Column(name = "longitude")
	open var longitude: Double? = null

	@Column(name = "data_standard_date")
	open var dataStandardDate: LocalDate? = null

	@Size(max = 50)
	@Column(name = "provider_instt_code", length = 50)
	open var providerInsttCode: String? = null

	@Size(max = 255)
	@Column(name = "provider_instt_name")
	open var providerInsttName: String? = null

	@Size(max = 500)
	@Column(name = "image", length = 500)
	open var image: String? = null

	fun toDto(category: List<String?>?=null): FestivalListResponseDto {
		return FestivalListResponseDto(
			id = this@Festival.id!!,
			festivalName = this@Festival.festivalName,
			holdPlace = this@Festival.holdPlace,
			festivalStartDate = this@Festival.festivalStartDate,
			festivalEndDate = this@Festival.festivalEndDate,
			rawContent = this@Festival.rawContent,
			operatorInstitution = this@Festival.operatorInstitution,
			hostInstitution = this@Festival.hostInstitution,
			sponsorInstitution = this@Festival.sponsorInstitution,
			tel = this@Festival.tel,
			homepageUrl = this@Festival.homepageUrl,
			relatedInfo = this@Festival.relatedInfo,
			roadAddress = this@Festival.roadAddress,
			landAddress = this@Festival.landAddress,
			latitude = this@Festival.latitude,
			longitude = this@Festival.longitude,
			dataStandardDate = this@Festival.dataStandardDate,
			providerInsttCode = this@Festival.providerInsttCode,
			providerInsttName = this@Festival.providerInsttName,
			image = this@Festival.image,
			like = false,
			category = category
		)
	}
}
