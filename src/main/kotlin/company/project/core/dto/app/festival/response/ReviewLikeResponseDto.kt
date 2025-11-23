package company.project.core.dto.app.festival.response

data class ReviewLikeResponseDto(
	val reviewId: Long,
	val likeCount: Long,
	val liked: Boolean
)
