package company.project.core.dto.app.festival.response

import company.project.core.entity.ReviewType
import java.time.LocalDateTime

data class FestivalReviewResponseDto(
	val id: Long,
	val festivalId: Long,
	val festivalName: String,
	val userName: String,
	val rating: Int,
	val content: String?,
	val type: ReviewType,
	val likeCount: Long,
	val liked: Boolean,
	val createdAt: LocalDateTime,
	val comments: List<ReviewCommentResponseDto>
)
