package company.project.main.app.festival.review

import company.project.core.dto.app.festival.request.FestivalReviewRequestDto
import company.project.core.dto.app.festival.response.FestivalReviewResponseDto
import company.project.core.entity.UserRole
import company.project.lib.common.annotation.Api
import company.project.lib.common.annotation.Auth
import company.project.lib.common.objects.ApiPaths
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMethod
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
		review: FestivalReviewRequestDto,
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
		review: FestivalReviewRequestDto,
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


}
