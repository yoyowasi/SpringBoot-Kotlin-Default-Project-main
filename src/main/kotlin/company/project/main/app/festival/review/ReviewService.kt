package company.project.main.app.festival.review

import company.project.core.dto.app.festival.request.FestivalReviewRequestDto
import company.project.core.dto.app.festival.request.ReviewCommentRequestDto
import company.project.core.dto.app.festival.response.FestivalLikeResponseDto
import company.project.core.dto.app.festival.response.FestivalReviewResponseDto
import company.project.core.dto.app.festival.response.ReviewCommentResponseDto
import company.project.core.dto.app.festival.response.ReviewLikeResponseDto
import company.project.core.entity.FestivalReview
import company.project.core.entity.FestivalReviewComment
import company.project.core.entity.FestivalReviewLike
import company.project.core.entity.UserFestivalLike
import company.project.infra.repository.FestivalRepository
import company.project.infra.repository.FestivalReviewCommentRepository
import company.project.infra.repository.FestivalReviewLikeRepository
import company.project.infra.repository.FestivalReviewRepository
import company.project.infra.repository.UserRepository
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
	private val festivalReviewRepository: FestivalReviewRepository,
	private val festivalReviewLikeRepository: FestivalReviewLikeRepository,
	private val reviewComponent: ReviewComponent,
	private val festivalReviewCommentRepository: FestivalReviewCommentRepository,
	private val userRepository: UserRepository
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
			this.user = userRepository.findByUid(user.uid)
			this.rating = review.rating
			this.content = review.content
			this.type = review.type
			this.createdAt = Instant.now()
			this.updatedAt = Instant.now()
		}
		festivalReviewRepository.save(review)
		return review.toResponse()
	}

	@Transactional()
	fun getReviews(festivalId: Long): List<FestivalReviewResponseDto> {
		val userTokenInfoDto = authComponent.getUserTokenInfoOrNull()

		return festivalReviewRepository.findAllByFestivalIdOrderByCreatedAtDesc(festivalId)
			.map {
				val reviewId = it.id ?: 0L
				val liked =
					userTokenInfoDto?.uid?.let { festivalReviewLikeRepository.findByReviewIdAndUserUid(reviewId, it) }

				it.toResponse().copy(
					liked = liked?.like == true,
					likeCount = reviewId.let { festivalId -> reviewComponent.getCount(festivalId) } ?: 0L,
					comments = festivalReviewCommentRepository.findAllByReviewIdOrderByCreatedAtAsc(reviewId)
						?.map { commentEntity -> commentEntity.toResponse() } ?: emptyList()
				)
			}
	}

	@Transactional
	fun updateReview(festivalReviewId: Long, review: FestivalReviewRequestDto): FestivalReviewResponseDto {
		val user = authComponent.getUserTokenInfo()

		val reviewEntity = festivalReviewRepository.findById(festivalReviewId)
			.orElseThrow { ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_NOT_FOUND) }

		if (reviewEntity.user?.uid  != user.uid) {
			throw ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_REVIEW_OWNERSHIP_MISMATCH)
		}
		reviewEntity.rating = review.rating
		reviewEntity.content = review.content
		reviewEntity.type = review.type
		reviewEntity.updatedAt = Instant.now()
		return reviewEntity.toResponse()
	}

	@Transactional
	fun deleteReview(festivalReviewId: Long) {
		val user = authComponent.getUserTokenInfo()
		val review = festivalReviewRepository.findById(festivalReviewId)
			.orElseThrow { ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_NOT_FOUND) }
		if (review.user?.uid != user.uid) {
			throw ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_REVIEW_DELETE_OWNERSHIP_MISMATCH)
		}
		festivalReviewRepository.delete(review)
	}

	@Transactional()
	fun getReviewsAll(page: Int): List<FestivalReviewResponseDto> {
		val userTokenInfoDto = authComponent.getUserTokenInfoOrNull()

		val pageRequest = PageRequest.of(
			(page - 1).coerceAtLeast(0),
			50,
			Sort.by(Sort.Direction.DESC, "createdAt")   // 최근순 정렬
		)

		val reviews = festivalReviewRepository.findAll(pageRequest)

		return reviews.content.map {
			val reviewId = it.id ?: 0L
			val liked =
				userTokenInfoDto?.uid?.let { festivalReviewLikeRepository.findByReviewIdAndUserUid(reviewId, it) }

			it.toResponse().copy(
				liked = liked?.like == true,
				likeCount = reviewId.let { festivalId -> reviewComponent.getCount(festivalId) } ?: 0L,
				comments = festivalReviewCommentRepository.findAllByReviewIdOrderByCreatedAtAsc(reviewId)
					?.map { commentEntity -> commentEntity.toResponse() } ?: emptyList()
			)
		}
	}

	@Transactional
	fun toggleReviewLike(reviewId: Long): ReviewLikeResponseDto {
		val user = authComponent.getUserTokenInfo()
		var likeToggle = false

		val exists = festivalReviewLikeRepository.existsByReviewIdAndUserUid(reviewId, user.uid) == true

		val count =
			if (!exists) {
				festivalReviewLikeRepository.save(FestivalReviewLike().apply {
					this.reviewId = reviewId
					this.userUid = user.uid
					this.like = true
					this.createdAt = Instant.now()
				})
				reviewComponent.incr(reviewId)
			} else {
				val likeEntity = festivalReviewLikeRepository.findByReviewIdAndUserUid(reviewId, user.uid)!!

				if(likeEntity.like == true) { // 좋아요 취소
					likeEntity.like = false
					likeToggle = festivalReviewLikeRepository.save(likeEntity).like == true
					reviewComponent.decr(reviewId)
				}else{ // 좋아요 다시 누르기
					likeEntity.like = true
					likeToggle = festivalReviewLikeRepository.save(likeEntity).like == true
					reviewComponent.incr(reviewId)
				}
			}

		return ReviewLikeResponseDto(
			reviewId = reviewId,
			likeCount = count,
			liked = likeToggle
		)
	}

	// ========== 댓글 ==========

	@Transactional
	fun addComment(reviewId: Long, content: String): ReviewCommentResponseDto {
		val user = authComponent.getUserTokenInfo()

		val review = festivalReviewRepository.findById(reviewId)
			.orElseThrow { ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_NOT_FOUND) }

		val comment = FestivalReviewComment().apply {
			this.review = review
			this.user = userRepository.findByUid(user.uid)
			this.content = content
			this.createdAt = Instant.now()
			this.updatedAt = Instant.now()

		}

		festivalReviewCommentRepository.save(comment)
		return comment.toResponse()
	}

	@Transactional
	fun getComments(reviewId: Long): List<ReviewCommentResponseDto>? =
		festivalReviewCommentRepository.findAllByReviewIdOrderByCreatedAtAsc(reviewId)
			?.map { it.toResponse() }

	@Transactional
	fun updateComment(commentId: Long, dto: ReviewCommentRequestDto): ReviewCommentResponseDto {
		val user = authComponent.getUserTokenInfo()

		val comment = festivalReviewCommentRepository.findById(commentId)
			.orElseThrow { ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_NOT_FOUND) }

		if (comment.user?.uid != user.uid)
			throw ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_REVIEW_OWNERSHIP_MISMATCH)

		comment.content = dto.content
		comment.updatedAt = Instant.now()

		return comment.toResponse()
	}

	@Transactional
	fun deleteComment(commentId: Long) {
		val user = authComponent.getUserTokenInfo()
		val comment = festivalReviewCommentRepository.findById(commentId)
			.orElseThrow { ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_NOT_FOUND) }
		if (comment.user?.uid != user.uid)
			throw ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_REVIEW_DELETE_OWNERSHIP_MISMATCH)
		festivalReviewCommentRepository.delete(comment)
	}
}
