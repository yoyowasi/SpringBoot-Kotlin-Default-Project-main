package company.project.main.scheduler

import company.project.lib.common.objects.FestivalTagKeywords
import company.project.lib.common.objects.TagDimension
import company.project.main.collection.FestivalCollection
import company.project.main.collection.FestivalSaveService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class FestivalCollectionScheduler(
	private val festivalCollection: FestivalCollection,
	private val festivalSaveService: FestivalSaveService
) {
//	@Scheduled(fixedDelay = 24 * 60 * 60 * 1000)  // 24시간마다 실행
//	@Scheduled(cron = "0 0 0 * * ?")	// 00시에 매일 실행
	fun festivalCollection() {
		val festivalData = festivalCollection.getJsonData()

		festivalData.records.map { record ->
			if (record.latitude.isNullOrBlank() || record.longitude.isNullOrBlank()) {
				val response = festivalCollection.getLatitudeLongtide(record)
				record.latitude = response?.documents?.firstOrNull()?.y ?: ""
				record.longitude = response?.documents?.firstOrNull()?.x ?: ""
			}
			val image = record.fstvlNm?.let {festivalCollection.searchFestivalImage(it)?.items?.firstOrNull()?.link  }

			val classifiedTags = parseAndClassifyRecord(record.fstvlCo) + parseAndClassifyRecord(record.fstvlNm)
			festivalSaveService.saveFestival(record, classifiedTags , image)
			return@map
		}
	}

	fun parseAndClassifyRecord(fstvlCo: String?): List<ClassifiedTagDto> {
		val SEPARATOR_REGEX = Regex("[,+·\\s]+")
		if (fstvlCo.isNullOrBlank()) return emptyList()

		val tokens = fstvlCo.split(SEPARATOR_REGEX)
			.map { it.trim() }
			.filter { it.isNotBlank() }

		return tokens.map { token ->
			val categories = classifyTag(token)
			ClassifiedTagDto(
				originalToken = token,
				categories = categories
			)
		}
	}

	fun classifyTag(token: String): List<TagCategoryDto> {
		val result = mutableListOf<TagCategoryDto>()
		val lower = token.lowercase()

		FestivalTagKeywords.CONTENT_KEYWORDS.forEach { (category, keywords) ->
			if (keywords.any { lower.contains(it) }) {
				result += TagCategoryDto(TagDimension.CONTENT, category)
			}
		}

		FestivalTagKeywords.EVENT_KEYWORDS.forEach { (category, keywords) ->
			if (keywords.any { lower.contains(it) }) {
				result += TagCategoryDto(TagDimension.EVENT_TYPE, category)
			}
		}

		FestivalTagKeywords.AUDIENCE_KEYWORDS.forEach { (category, keywords) ->
			if (keywords.any { lower.contains(it) }) {
				result += TagCategoryDto(TagDimension.AUDIENCE, category)
			}
		}

		FestivalTagKeywords.THEME_KEYWORDS.forEach { (category, keywords) ->
			if (keywords.any { lower.contains(it) }) {
				result += TagCategoryDto(TagDimension.THEME, category)
			}
		}

		if (result.isEmpty()) {
			result += TagCategoryDto(TagDimension.ETC, "기타")
		}

		return result
	}

	data class TagCategoryDto(
		val dimension: TagDimension,
		val category: String
	)

	data class ClassifiedTagDto(
		val originalToken: String,
		val categories: List<TagCategoryDto>
	)
} // ← 이게 누락되어 있었음!
