package company.project.main.large.common.test

import java.util.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import company.project.lib.annotation.LargeTest
import company.project.lib.common.dto.ServerErrorResponseDto
import company.project.lib.common.enum.INTERNAL_ERROR_CODE
import company.project.lib.common.objects.ApiPaths
import company.project.lib.common.util.Memory
import company.project.lib.util.TestRestApiTemplate

@LargeTest
class TestControllerSpec(
	@LocalServerPort private val port: Int,
	@Autowired private val memory: Memory,
) {
	@Test
	fun `서버 정상 작동 확인`() {
		// given
		val path = ApiPaths.LOCALHOST + port + ApiPaths.HEALTH

		// when
		val response = TestRestApiTemplate().get(path, Int::class.java)

		// then
		Assertions.assertEquals(response.statusCode, HttpStatus.OK)
		Assertions.assertEquals(response.body, 200)
	}

	@Test
	fun `에러 테스트`() {
		// given
		val path = ApiPaths.LOCALHOST + port + ApiPaths.ERROR

		// when
		val response = TestRestApiTemplate().get(path, ServerErrorResponseDto::class.java)

		// then
		Assertions.assertEquals(response.statusCode, INTERNAL_ERROR_CODE.TEST.httpStatus)
		Assertions.assertEquals(response.body, INTERNAL_ERROR_CODE.TEST.mappingServerErrorResponseDto())
	}

	@Test
	fun `UserLevelLock 테스트`() {
		// given
		val path = ApiPaths.LOCALHOST + port + ApiPaths.USER_LEVEL_LOCK
		val uid = UUID.randomUUID().toString()

		// when
		val response = TestRestApiTemplate().get(path, Int::class.java, uid)

		// then
		Assertions.assertEquals(response.statusCode, HttpStatus.OK)
		Assertions.assertEquals(response.body, 200)
	}
}
