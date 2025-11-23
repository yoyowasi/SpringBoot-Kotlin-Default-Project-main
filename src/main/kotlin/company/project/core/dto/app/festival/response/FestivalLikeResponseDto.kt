package company.project.core.dto.app.festival.response

data class FestivalLikeResponseDto(
	val festivalId: Long,
	val like: Boolean,
	val likeCount: Long
)
