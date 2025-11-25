package company.project.main.app.festival.review

import company.project.core.dto.app.festival.request.FestivalReviewRequestDto
import company.project.core.dto.app.festival.request.ReviewCommentRequestDto
import company.project.core.dto.app.festival.response.FestivalReviewResponseDto
import company.project.core.dto.app.festival.response.ReviewCommentResponseDto
import company.project.core.dto.app.festival.response.ReviewLikeResponseDto
import company.project.core.entity.UserRole
import company.project.lib.common.annotation.Api
import company.project.lib.common.annotation.Auth
import company.project.lib.common.objects.ApiPaths
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Festival Review", description = "축제 리뷰")
@RestController
class ReviewController(private val reviewService: ReviewService) {

	@Api(
		path = ApiPaths.FESTIVAL_REVIEW_LIST,
		method = RequestMethod.GET,
		summary = "축제 리뷰 리스트",
		description = "축제 리뷰 리스트 입니다.",
	)
	fun festivalReviewListAll(
		@RequestParam page: Int,
	): ResponseEntity<List<FestivalReviewResponseDto>> {
		val result = reviewService.getReviewsAll(page)
		return ResponseEntity.ok().body(result)
	}

	@Api(
		path = ApiPaths.FESTIVAL_REVIEW_LIST_SELECTED,
		method = RequestMethod.GET,
		summary = "특정 축제 리뷰 리스트",
		description = "특정 축제 리뷰 리스트 입니다.",
	)
	fun festivalReviewList(
		@PathVariable festivalId: Long,
	): ResponseEntity<List<FestivalReviewResponseDto>> {
		val result = reviewService.getReviews(festivalId)
		return ResponseEntity.ok().body(result)
	}

	@Api(
		path = ApiPaths.FESTIVAL_REVIEW_CREATE,
		method = RequestMethod.POST,
		summary = "축제 리뷰 작성",
		description = "축제 리뷰 작성 입니다.",
	)
	@Auth(role = UserRole.USER)
	fun createFestivalReview(
		@PathVariable festivalId: Long,
		@RequestBody review: FestivalReviewRequestDto,
	): ResponseEntity<FestivalReviewResponseDto> {
		val result = reviewService.createReview(festivalId, review)
		return ResponseEntity.ok().body(result)
	}

	@Api(
		path = ApiPaths.FESTIVAL_REVIEW_DETAIL,
		method = RequestMethod.PUT,
		summary = "축제 리뷰 수정",
		description = "축제 리뷰 수정 입니다.",
	)
	@Auth(role = UserRole.USER)
	fun updateFestivalReview(
		@PathVariable festivalReviewId: String,
		@RequestBody review: FestivalReviewRequestDto,
	): ResponseEntity<FestivalReviewResponseDto> {
		val result = reviewService.updateReview(festivalReviewId.toLong(), review)
		return ResponseEntity.ok().body(result)
	}

	@Api(
		path = ApiPaths.FESTIVAL_REVIEW_DETAIL,
		method = RequestMethod.DELETE,
		summary = "축제 리뷰 삭제",
		description = "축제 리뷰 삭제 입니다.",
	)
	@Auth(role = UserRole.USER)
	fun deleteFestivalReview(
		@PathVariable festivalReviewId: String,
	): ResponseEntity<Unit> {
		reviewService.deleteReview(festivalReviewId.toLong())
		return ResponseEntity.ok().build()
	}

	// ========= 리뷰 좋아요 =========
	@Api(
		path = ApiPaths.FESTIVAL_REVIEW_LIKE,
		method = RequestMethod.POST,
		summary = "리뷰 좋아요 토글",
		description = "리뷰 좋아요/취소 토글"
	)
	@Auth(role = UserRole.USER)
	fun toggleReviewLike(
		@PathVariable reviewId: Long
	): ResponseEntity<ReviewLikeResponseDto> {
		return ResponseEntity.ok(reviewService.toggleReviewLike(reviewId))
	}

	// ========= 리뷰 댓글 =========
	@Api(
		path = ApiPaths.FESTIVAL_REVIEW_COMMENT,
		method = RequestMethod.POST,
		summary = "리뷰 댓글 작성",
		description = "리뷰에 댓글을 작성합니다."
	)
	@Auth(role = UserRole.USER)
	fun createReviewComment(
		@PathVariable reviewId: Long,
		@RequestBody dto: ReviewCommentRequestDto
	): ResponseEntity<ReviewCommentResponseDto> {
		return ResponseEntity.ok(reviewService.addComment(reviewId, dto.content))
	}

	@Api(
		path = ApiPaths.FESTIVAL_REVIEW_COMMENT,
		method = RequestMethod.GET,
		summary = "리뷰 댓글 목록",
		description = "특정 리뷰의 댓글 리스트"
	)
	fun getReviewComments(
		@PathVariable reviewId: Long
	): ResponseEntity<List<ReviewCommentResponseDto>> {
		return ResponseEntity.ok(reviewService.getComments(reviewId))
	}

	@Api(
		path = ApiPaths.FESTIVAL_REVIEW_COMMENT_DETAIL,
		method = RequestMethod.PUT,
		summary = "리뷰 댓글 수정",
		description = "작성자가 댓글 수정"
	)
	@Auth(role = UserRole.USER)
	fun updateReviewComment(
		@PathVariable commentId: Long,
		@RequestBody dto: ReviewCommentRequestDto
	): ResponseEntity<ReviewCommentResponseDto> {
		return ResponseEntity.ok(reviewService.updateComment(commentId, dto))
	}

	@Api(
		path = ApiPaths.FESTIVAL_REVIEW_COMMENT_DETAIL,
		method = RequestMethod.DELETE,
		summary = "리뷰 댓글 삭제",
		description = "작성자가 댓글 삭제"
	)
	@Auth(role = UserRole.USER)
	fun deleteReviewComment(
		@PathVariable commentId: Long
	): ResponseEntity<Unit> {
		reviewService.deleteComment(commentId)
		return ResponseEntity.ok().build()
	}
}
