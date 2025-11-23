package company.project.main.app.festival.main

import company.project.core.dto.app.festival.request.FestivalReviewRequestDto
import company.project.core.dto.app.festival.response.FestivalLikeResponseDto
import company.project.core.dto.app.festival.response.FestivalListResponseDto
import company.project.core.dto.app.festival.response.FestivalReviewResponseDto
import company.project.core.entity.UserRole
import company.project.lib.common.annotation.Api
import company.project.lib.common.annotation.Auth
import company.project.lib.common.objects.ApiPaths
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Festival", description = "축제")
@RestController
class FestivalController(
	private val festivalService: FestivalService,
) {
	@Api(
		path = ApiPaths.FESTIVAL_LIST,
		method = RequestMethod.GET,
		summary = "축제 리스트",
		description = "축제 리스트 입니다.",
	)
	fun festivalList(
		@RequestParam page: Int,
	): ResponseEntity<List<FestivalListResponseDto>> {
		val result = festivalService.getList(page)
		return ResponseEntity.ok().body(result)
	}

	@Api(
		path = ApiPaths.FestIVAL_DETAIL,
		method = RequestMethod.GET,
		summary = "축제 상세 보기",
		description = "축제 상세 보기 입니다.",
	)
	fun festivalDetail(
		@PathVariable festivalId: Long,
	): ResponseEntity<FestivalListResponseDto> {
		val result = festivalService.get(festivalId)
		return ResponseEntity.ok().body(result)
	}

	@Api(
		path = ApiPaths.FESTIVAL_SEARCH,
		method = RequestMethod.GET,
		summary = "축제 검색 보기",
		description = "축제 검색 입니다.",
	)
	fun festivalSearch(
		@PathVariable keyword: String,
	): ResponseEntity<List<FestivalListResponseDto?>?> {
		val result = festivalService.getSearch(keyword)
		return ResponseEntity.ok().body(result)
	}

	@Api(
		path = ApiPaths.FESTIVAL_TOGGLE_LIKE,
		method = RequestMethod.POST,
		summary = "축제 좋아요 토글",
		description = "축제 좋아요 토글 입니다.",
	)
	@Auth(role = UserRole.USER)
	fun toggleFestivalLike(
		@PathVariable festivalId: Long,
	): ResponseEntity<FestivalLikeResponseDto?> {
		val result = festivalService.toggleFestivalLike(festivalId)
		return ResponseEntity.ok().body(result)
	}
}
