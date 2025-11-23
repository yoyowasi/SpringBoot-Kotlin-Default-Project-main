package company.project.core.dto.app.festival.response

import java.time.LocalDateTime

data class ReviewCommentResponseDto(
	val commentId: Long,
	val reviewId: Long,
	val userName: String,
	val content: String,
	val createdAt: LocalDateTime,
	val updatedAt: LocalDateTime
)
