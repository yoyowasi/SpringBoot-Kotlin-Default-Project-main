package company.project.infra.repository

import company.project.core.entity.FestivalReview
import org.springframework.data.jpa.repository.JpaRepository

interface FestivalReviewRepository : JpaRepository<FestivalReview, Long> {
	fun findAllByFestivalIdOrderByCreatedAtDesc(festivalId: Long): List<FestivalReview>

	fun existsByIdAndUserUid(id: Long, userUid: String): Boolean
}
