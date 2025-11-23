package company.project.lib.common.component

import java.util.UUID
import net.datafaker.Faker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import company.project.core.entity.UserRole
import company.project.infra.repository.UserRepository
import company.project.lib.common.dto.UserTokenInfoDto
import company.project.lib.common.enum.INTERNAL_ERROR_CODE
import company.project.lib.common.exception.ServerErrorException
import company.project.lib.common.objects.Profiles
import company.project.lib.common.util.Etc
import company.project.lib.common.util.Jwt
import company.project.lib.common.util.Memory

@Component
class AuthComponent(
	private val jwt: Jwt,
	@Autowired private val etc: Etc,
	@Autowired private val memory: Memory,
	private val userRepository: UserRepository,
) {
	/**
	 * [AuthComponent]
	 * - firebase 인증 관련 컴포넌트
	 */
	fun getUserTokenInfo(role: UserRole? = UserRole.USER): UserTokenInfoDto {
		val requestAttributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
		val request = requestAttributes.request
		val authorization = request.getHeader("Authorization")
		val token = authorization?.substring(7)

		return kotlin.runCatching {
			if (etc.getActiveProfile() != Profiles.NOT_TEST) {
				val customToken = jwt.validateToken(token)

				when (role) {
					UserRole.ADMIN -> {
						userRepository.findByUid(customToken.uid).let { userEntity ->
							if (userEntity?.role != UserRole.ADMIN) {
								throw ServerErrorException(INTERNAL_ERROR_CODE.NOT_ADMIN)
							}
						}
					}
					else -> { }
				}

				UserTokenInfoDto(
					uid = customToken.uid,
					email = customToken.email,
					name = customToken.name,
					isEmailVerified = customToken.isEmailVerified,
					picture = customToken.picture,
				)
			} else {
				val faker = Faker()
				UserTokenInfoDto(
					uid = memory.getUUID() ?: UUID.randomUUID().toString(),
					email = faker.internet().emailAddress(),
					name = faker.name().name(),
					isEmailVerified = faker.bool().bool(),
					picture = faker.internet().image(),
				)
			}
		}.getOrElse {
			if (it is ServerErrorException) {
				throw it
			} else {
				throw ServerErrorException(INTERNAL_ERROR_CODE.INVALID_TOKEN)
			}
		}
	}
}
