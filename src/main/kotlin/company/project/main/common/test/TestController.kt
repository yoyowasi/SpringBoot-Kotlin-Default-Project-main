package company.project.main.common.test

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import company.project.lib.common.annotation.Api
import company.project.lib.common.annotation.Auth
import company.project.lib.common.annotation.UserLevelLock
import company.project.lib.common.enum.INTERNAL_ERROR_CODE
import company.project.lib.common.exception.ServerErrorException
import company.project.lib.common.objects.ApiPaths

@Tag(name = "Test", description = "테스트")
@RestController
class TestController {
	@Api(
		path = ApiPaths.HEALTH,
		method = RequestMethod.GET,
		summary = "헬스체크",
		description = "서버의 상태를 확인합니다.",
	)
	fun health(): ResponseEntity<Int> {
		return ResponseEntity.ok().body(200)
	}

	@Api(
		path = ApiPaths.ERROR,
		method = RequestMethod.GET,
		summary = "에러 테스트",
		description = "에러를 발생시킵니다.",
	)
	fun error(): ResponseEntity<Int> {
		throw ServerErrorException(INTERNAL_ERROR_CODE.TEST)
	}

	@UserLevelLock
	@Auth
	@Api(
		path = ApiPaths.USER_LEVEL_LOCK,
		method = RequestMethod.GET,
		summary = "유저 레벨 락",
		description = "유저 레벨 락을 테스트합니다.",
	)
	fun userLevelLock(): ResponseEntity<Int> {
		Thread.sleep(2000)
		return ResponseEntity.ok().body(200)
	}
}
