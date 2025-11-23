package company.project.core.dto.app.festival.request

import company.project.core.entity.ReviewType

data class FestivalReviewRequestDto(
	val rating: Int,
	val content: String?,
	val type: ReviewType,
)
