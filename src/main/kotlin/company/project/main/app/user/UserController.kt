package company.project.main.app.user

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import company.project.core.dto.app.user.*
import company.project.core.dto.common.user.UserCommonDto
import company.project.core.entity.UserRole
import company.project.lib.common.annotation.Api
import company.project.lib.common.annotation.Auth
import company.project.lib.common.objects.ApiPaths

@Tag(name = "User", description = "유저")
@RestController
class UserController(
	private val userService: UserService,
) {
	@Auth(role = UserRole.ADMIN)
	@Api(
		path = ApiPaths.PROFILE,
		method = RequestMethod.GET,
		summary = "프로필 조회(자신) check2",
		description = "자기 자신의 프로필을 조회합니다.",
	)
	fun getProfile(): ResponseEntity<UserCommonDto> {
		val userProfile = userService.getProfile()

		return ResponseEntity.ok().body(userProfile)
	}

	@Api(
		path = ApiPaths.SIGN_UP,
		method = RequestMethod.POST,
		summary = "회원가입",
		description = "회원가입을 진행합니다.",
	)
	fun signUp(
		@Valid @RequestBody userSignUpInDto: UserSignUpInDto,
	): ResponseEntity<UserCommonDto> {
		val userSignUpOutDto =
			userService.signUp(
				userSignUpInDto = userSignUpInDto,
			)

		return ResponseEntity.ok().body(userSignUpOutDto)
	}

	@Api(
		path = ApiPaths.LOGIN,
		method = RequestMethod.POST,
		summary = "로그인",
		description = "로그인합니다.",
	)
	fun login(
		@RequestBody userLoginInDto: UserLoginInDto,
	): ResponseEntity<UserLoginOutDto> {
		val userSingInOutDto = userService.login(userLoginInDto)

		return ResponseEntity.ok().body(userSingInOutDto)
	}

	@Api(
		path = ApiPaths.REFRESH_TOKEN,
		method = RequestMethod.POST,
		summary = "토큰 갱신",
		description = "토큰을 갱신합니다.",
	)
	fun refreshToken(
		@RequestBody userRefreshTokenInDto: UserRefreshTokenInDto,
	): ResponseEntity<UserRefreshTokenOutDto> {
		val userSingInOutDto = userService.refreshToken(userRefreshTokenInDto)

		return ResponseEntity.ok().body(userSingInOutDto)
	}
}
