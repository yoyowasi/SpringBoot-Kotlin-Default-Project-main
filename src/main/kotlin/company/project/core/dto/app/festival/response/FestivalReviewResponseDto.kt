package company.project.core.dto.app.festival.response

import java.time.LocalDateTime

data class FestivalReviewResponseDto(
	val id: Long,
	val userUid: String,
	val rating: Int,
	val content: String?,
	val createdAt: LocalDateTime,
)
