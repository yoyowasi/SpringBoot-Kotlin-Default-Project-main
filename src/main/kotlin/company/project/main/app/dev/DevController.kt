package company.project.main.app.dev

import company.project.core.entity.UserRole
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import company.project.lib.common.annotation.Api
import company.project.lib.common.annotation.Auth
import company.project.lib.common.objects.ApiPaths
import company.project.lib.common.objects.Profiles
import company.project.lib.common.util.SHAType
import company.project.lib.common.util.Sha

@Profile(Profiles.NOT_PROD)
@Tag(name = "Dev", description = "개발")
@RestController
class DevController(
	private val sha: Sha,
) {
	@Auth
	@Api(
		path = ApiPaths.GET_PW,
		method = RequestMethod.GET,
		summary = "비밀번호 조회",
		description = "비밀번호를 조회합니다.",
	)
	fun getPW(
		@PathVariable pw: String,
	): ResponseEntity<String> = ResponseEntity.ok().body(sha.encrypt(pw, SHAType.SHA256))

	@Api(
		path = ApiPaths.CPU_BURN,
		method = RequestMethod.GET,
		summary = "CPU 부하 테스트",
		description = "CPU 부하 테스트를 진행합니다.",
	)
	@Auth(role = UserRole.ADMIN)
	fun cpuBurn(
		@PathVariable seconds: Long,
	): ResponseEntity<String> {
		val end = System.currentTimeMillis() + (seconds * 1000)
		// CPU fully busy loop
		while (System.currentTimeMillis() < end) {
			Math.sqrt(Math.random())
		}
		return ResponseEntity.ok().body("burned $seconds seconds")
	}
}
