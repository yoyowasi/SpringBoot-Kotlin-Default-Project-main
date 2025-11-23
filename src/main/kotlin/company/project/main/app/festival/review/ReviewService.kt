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
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
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

		val exist = festivalReviewRepository.existsByFestivalIdAndUserUid(festivalId, user.uid)

		if(exist){
			throw ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_REVIEW_ALREADY_EXISTS)
		}

		val review = FestivalReview().apply {
			this.festival = festival
			this.userUid = user.uid
			this.rating = review.rating
			this.content = review.content
			this.createdAt = Instant.now()
			this.updatedAt = Instant.now()
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
	fun updateReview(festivalReviewId: Long, review: FestivalReviewRequestDto): FestivalReviewResponseDto {
		val user = authComponent.getUserTokenInfo()

		val reviewEntity = festivalReviewRepository.findById(festivalReviewId)
			.orElseThrow { ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_NOT_FOUND) }

		if (reviewEntity.userUid != user.uid) {
			throw ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_REVIEW_OWNERSHIP_MISMATCH)
		}
		reviewEntity.rating = review.rating
		reviewEntity.content = review.content
		reviewEntity.updatedAt = Instant.now()
		return reviewEntity.toResponse()
	}

	@Transactional
	fun deleteReview(festivalReviewId: Long) {
		val user = authComponent.getUserTokenInfo()
		val review = festivalReviewRepository.findById(festivalReviewId)
			.orElseThrow { ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_NOT_FOUND) }
		if (review.userUid != user.uid) {
			throw ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_REVIEW_DELETE_OWNERSHIP_MISMATCH)
		}
		festivalReviewRepository.delete(review)
	}

	@Transactional()
	fun getReviewsAll(page: Int): List<FestivalReviewResponseDto> {
		val pageRequest = PageRequest.of(
			(page - 1).coerceAtLeast(0),
			50,
			Sort.by(Sort.Direction.DESC, "createdAt")   // 최근순 정렬
		)

		val reviews = festivalReviewRepository.findAll(pageRequest)

		return reviews.content.map { it.toResponse() }
	}
}
