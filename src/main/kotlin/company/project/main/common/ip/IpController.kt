package company.project.main.common.ip

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import company.project.core.entity.UserRole
import company.project.lib.common.annotation.Api
import company.project.lib.common.annotation.Auths
import company.project.lib.common.objects.ApiPaths

@Auths(role = UserRole.ADMIN)
@Tag(name = "Ip", description = "관리자 ip")
@RestController
class IpController(
	private val ipService: IpService,
) {
	@Api(
		path = ApiPaths.INIT_IP,
		method = RequestMethod.GET,
		summary = "IP 초기화",
		description = "IP 초기화",
	)
	fun initIp(): ResponseEntity<Nothing> {
		ipService.initIp()

		return ResponseEntity.ok().body(null)
	}
}
