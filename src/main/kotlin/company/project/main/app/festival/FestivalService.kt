package company.project.main.app.festival

import company.project.core.dto.app.festival.FestivalListResponseDto
import company.project.infra.repository.FestivalRepository
import company.project.infra.repository.FestivalTagMapRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class FestivalService(
	private val festivalRepository: FestivalRepository,
	val festivalTagMapRepository: FestivalTagMapRepository
) {
	@Transactional
	fun getList(page: Int): List<FestivalListResponseDto> {
		// 페이지 번호가 1부터 온다고 가정하면 -1, 0부터면 그대로 사용
		val pageRequest = PageRequest.of(page - 1, 20) // size = 20 예시

		val festivals = festivalRepository.findAll(pageRequest)

		return festivals.content.map { festival ->
			val festivalId = festival.id
			val category = festivalTagMapRepository.findAllByFestivalId(festivalId!!).map {
				it.tag?.category
			}.distinct()
			FestivalListResponseDto(
				festivalName = festival.festivalName,
				holdPlace = festival.holdPlace,
				festivalStartDate = festival.festivalStartDate,
				festivalEndDate = festival.festivalEndDate,
				rawContent = festival.rawContent,
				operatorInstitution = festival.operatorInstitution,
				hostInstitution = festival.hostInstitution,
				sponsorInstitution = festival.sponsorInstitution,
				tel = festival.tel,
				homepageUrl = festival.homepageUrl,
				relatedInfo = festival.relatedInfo,
				roadAddress = festival.roadAddress,
				landAddress = festival.landAddress,
				latitude = festival.latitude,
				longitude = festival.longitude,
				dataStandardDate = festival.dataStandardDate,
				providerInsttCode = festival.providerInsttCode,
				providerInsttName = festival.providerInsttName,
				image = festival.image,
				category = category
			)
		}
	}
}
