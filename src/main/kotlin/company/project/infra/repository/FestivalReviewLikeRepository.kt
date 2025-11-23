package company.project.infra.repository

import company.project.core.entity.FestivalReviewLike
import org.springframework.data.jpa.repository.JpaRepository

interface FestivalReviewLikeRepository : JpaRepository<FestivalReviewLike, Long> {
	fun existsByReviewIdAndUserUid(reviewId: Long?, userUid: String?): Boolean?
	fun findByReviewIdAndUserUid(reviewId: Long, userUid: String): FestivalReviewLike?
}
