package company.project.main.scheduler

import company.project.lib.common.objects.FestivalTagKeywords
import company.project.lib.common.objects.TagDimension
import company.project.main.collection.FestivalCollection
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class FestivalCollectionScheduler(
	private val festivalCollection: FestivalCollection,
) {
//	@Scheduled(cron = "0 0 0,12 * * *")
	@Scheduled(fixedDelay = 1000000)
fun festivalCollection() {
		val festivalData = festivalCollection.getJsonData()

		val classifiedRecords = festivalData.records.map { record ->
			val classifiedTags = parseAndClassifyRecord(record.fstvlCo)

			mapOf(
				"festivalName" to record.fstvlNm,
				"tags" to classifiedTags
			)
		}

		val invalidTags = classifiedRecords.flatMap { item ->
//			println("축제명: ${item["festivalName"]}")
//			println("분류된 태그:")

			@Suppress("UNCHECKED_CAST")
			val tags = item["tags"] as List<ClassifiedTagDto>

			val token =
				tags.flatMap { ct ->
					ct.categories
						.filter { it.dimension == TagDimension.ETC }
						.map { categoryDto ->
							ct.originalToken
						}
				}
			token.distinct()
		}.distinct()
		println(invalidTags)
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

	// 1) 콘텐츠
	FestivalTagKeywords.CONTENT_KEYWORDS.forEach { (category, keywords) ->
		if (keywords.any { lower.contains(it) }) {
			result += TagCategoryDto(
				dimension = TagDimension.CONTENT,
				category = category
			)
		}
	}

	// 2) 행사 형태
	FestivalTagKeywords.EVENT_KEYWORDS.forEach { (category, keywords) ->
		if (keywords.any { lower.contains(it) }) {
			result += TagCategoryDto(
				dimension = TagDimension.EVENT_TYPE,
				category = category
			)
		}
	}

	// 3) 대상
	FestivalTagKeywords.AUDIENCE_KEYWORDS.forEach { (category, keywords) ->
		if (keywords.any { lower.contains(it) }) {
			result += TagCategoryDto(
				dimension = TagDimension.AUDIENCE,
				category = category
			)
		}
	}

	// 4) 테마
	FestivalTagKeywords.THEME_KEYWORDS.forEach { (category, keywords) ->
		if (keywords.any { lower.contains(it) }) {
			result += TagCategoryDto(
				dimension = TagDimension.THEME,
				category = category
			)
		}
	}

	// 아무것도 안 걸리면 ETC
	if (result.isEmpty()) {
		result += TagCategoryDto(
			dimension = TagDimension.ETC,
			category = "기타"
		)
	}

	return result
}
// 한 태그가 어떤 분류들에 속하는지
data class TagCategoryDto(
	val dimension: TagDimension, // CONTENT / EVENT_TYPE / AUDIENCE / THEME / ETC
	val category: String         // "자연", "음식", "전시", "가족/어린이" 등
)

// 축제 설명에 나오는 "토큰 하나"에 대한 전체 분류 결과
data class ClassifiedTagDto(
	val originalToken: String,         // 예: "봄꽃축제", "사과 시식회"
	val categories: List<TagCategoryDto>
)
