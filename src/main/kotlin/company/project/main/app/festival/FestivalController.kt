package company.project.main.app.festival

import company.project.core.dto.app.festival.FestivalListResponseDto
import company.project.core.dto.app.user.UserSignUpInDto
import company.project.core.dto.common.user.UserCommonDto
import company.project.core.entity.UserRole
import company.project.lib.common.annotation.Api
import company.project.lib.common.annotation.Auth
import company.project.lib.common.objects.ApiPaths
import company.project.main.app.user.UserService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "User", description = "유저")
@RestController
class FestivalController(
	private val festivalService: FestivalService,
) {
	@Api(
		path = ApiPaths.FESTIVAL_LIST,
		method = RequestMethod.POST,
		summary = "메인페이지 축제 리스트",
		description = "메인페이지 축제 리스트 입니다.",
	)
	fun festivalList(
		@RequestParam page: Int,
	): ResponseEntity<List<FestivalListResponseDto>> {
		val result = festivalService.getList(page)
		return ResponseEntity.ok().body(result)
	}
}
