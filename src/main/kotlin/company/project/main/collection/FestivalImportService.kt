package company.project.main.collection

import company.project.core.dto.festival.FestivalRecordDto
import company.project.core.entity.Festival
import company.project.core.entity.FestivalTagMap
import company.project.core.entity.FestivalTagMaster
import company.project.infra.repository.FestivalRepository
import company.project.infra.repository.FestivalTagMapRepository
import company.project.infra.repository.FestivalTagMasterRepository
import company.project.main.scheduler.FestivalCollectionScheduler
import jakarta.transaction.Transactional
import java.time.LocalDate
import org.springframework.stereotype.Service
@Service
class FestivalSaveService(
	private val festivalRepository: FestivalRepository,
	private val festivalTagMasterRepository: FestivalTagMasterRepository,
	private val festivalTagMapRepository: FestivalTagMapRepository
) {

	@Transactional
	fun saveFestival(record: FestivalRecordDto, tags: List<FestivalCollectionScheduler.ClassifiedTagDto>, image: String? = null) {

		// 1) Festival 저장
		var festival = festivalRepository.save(
			Festival().apply {
				festivalName = record.fstvlNm
				holdPlace = record.holdPlace
				festivalStartDate = record.fstvlStartDate?.let { LocalDate.parse(it) }
				festivalEndDate = record.fstvlEndDate?.let { LocalDate.parse(it) }
				rawContent = record.fstvlCo
				operatorInstitution = record.oprtInstitNm
				hostInstitution = record.hostInstitNm
				sponsorInstitution = record.sponInstitNm
				tel = record.telno
				homepageUrl = record.homepageUrl
				relatedInfo = record.relInfo
				roadAddress = record.rdnmadr
				landAddress = record.lnmadr
				latitude = record.latitude?.toDoubleOrNull()
				longitude = record.longitude?.toDoubleOrNull()
				dataStandardDate = record.dataStdDe?.let { LocalDate.parse(it) }
				providerInsttCode = record.providerInsttCode
				providerInsttName = record.providerInsttNm
				this.image = image
			}
		)

		// 2) 태그 저장 및 매핑
		tags.forEach { tagDto ->

			tags.forEach { tagDto ->
				tagDto.categories.forEach { categoryString ->

					val dimension = categoryString.dimension.name   // CONTENT / EVENT_TYPE / ...
					val category = categoryString.category          // 자연, 음식, 환경, ...

					val tagMaster = festivalTagMasterRepository.findByDimensionAndCategoryAndKeyword(
						dimension,
						category,
						tagDto.originalToken
					) ?: festivalTagMasterRepository.save(
						FestivalTagMaster().apply {
							this.dimension = dimension
							this.category = category
							this.keyword = tagDto.originalToken
							this.displayName = tagDto.originalToken
						}
					)

					if (!festivalTagMapRepository.existsByFestivalIdAndTagId(festival.id!!, tagMaster.id!!)) {
						festivalTagMapRepository.save(
							FestivalTagMap().apply {
								this.festival = festival
								this.tag = tagMaster
								this.originalToken = tagDto.originalToken
							}
						)
					}
				}
			}
		}
	}
}
