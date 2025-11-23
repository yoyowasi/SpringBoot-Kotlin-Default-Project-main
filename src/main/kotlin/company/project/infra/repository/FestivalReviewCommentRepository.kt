package company.project.infra.repository

import company.project.core.entity.FestivalReviewComment
import org.springframework.data.jpa.repository.JpaRepository

interface FestivalReviewCommentRepository : JpaRepository<FestivalReviewComment, Long> {
	fun findAllByReviewIdOrderByCreatedAtAsc(reviewId: Long): List<FestivalReviewComment>?
}
