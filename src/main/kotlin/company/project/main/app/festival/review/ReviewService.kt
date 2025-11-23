package company.project.main.app.festival.review

import company.project.core.dto.app.festival.request.FestivalReviewRequestDto
import company.project.core.dto.app.festival.response.FestivalReviewResponseDto
import company.project.core.entity.FestivalReview
import company.project.infra.repository.FestivalRepository
import company.project.infra.repository.FestivalReviewRepository
import company.project.lib.common.component.AuthComponent
import company.project.lib.common.enum.INTERNAL_ERROR_CODE
import company.project.lib.common.exception.ServerErrorException
import jakarta.transaction.Transactional
import java.time.Instant
import org.springframework.stereotype.Service


@Service
class ReviewService(
	val authComponent: AuthComponent,
	private val festivalRepository: FestivalRepository,
	private val festivalReviewRepository: FestivalReviewRepository
) {
	@Transactional
	fun createReview(festivalId: Long, review: FestivalReviewRequestDto): FestivalReviewResponseDto {
		val user = authComponent.getUserTokenInfo()
		val festival = festivalRepository.findById(festivalId)
			.orElseThrow { ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_NOT_FOUND) }

		val review = FestivalReview().apply {
			this.festival = festival
			this.userUid = user.uid
			this.rating = review.rating
			this.content = review.content
		}
		festivalReviewRepository.save(review)
		return review.toResponse()
	}

	@Transactional()
	fun getReviews(festivalId: Long): List<FestivalReviewResponseDto> {
		return festivalReviewRepository.findAllByFestivalIdOrderByCreatedAtDesc(festivalId)
			.map { it.toResponse() }
	}

	@Transactional
	fun updateReview(reviewId: Long, review: FestivalReviewRequestDto): FestivalReviewResponseDto {
		val user = authComponent.getUserTokenInfo()

		val review = festivalReviewRepository.findById(reviewId)
			.orElseThrow { ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_NOT_FOUND) }

		if (review.userUid != user.uid) {
			throw ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_REVIEW_OWNERSHIP_MISMATCH)
		}
		review.rating = review.rating
		review.content = review.content
		review.updatedAt = Instant.now()
		return review.toResponse()
	}

	@Transactional
	fun deleteReview(reviewId: Long) {
		val user = authComponent.getUserTokenInfo()
		val review = festivalReviewRepository.findById(reviewId)
			.orElseThrow { ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_NOT_FOUND) }
		if (review.userUid != user.uid) {
			throw ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_REVIEW_DELETE_OWNERSHIP_MISMATCH)
		}
		festivalReviewRepository.delete(review)
	}
}
