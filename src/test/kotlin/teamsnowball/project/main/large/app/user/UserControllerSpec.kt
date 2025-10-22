package company.project.main.large.app.user

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import company.project.core.dto.common.user.UserCommonDto
import company.project.lib.annotation.LargeTest
import company.project.lib.common.objects.ApiPaths
import company.project.lib.common.util.Memory
import company.project.lib.mock.UserMock
import company.project.lib.util.TestRestApiTemplate

@LargeTest
class UserControllerSpec(
	@LocalServerPort private val port: Int,
	@Autowired private val memory: Memory,
	@Autowired private val userMock: UserMock,
) {
	val userData = userMock.makeMockUser(1)

	@Test
	fun `프로필 조회`() {
		// setting
		val profilePath = ApiPaths.LOCALHOST + port + ApiPaths.PROFILE

		// given
		memory.saveUUID(userData.first().uid.toString())

		// when
		val response = TestRestApiTemplate().get(profilePath, UserCommonDto::class.java)

		// then
		Assertions.assertEquals(response.statusCode, HttpStatus.OK)
	}
}
