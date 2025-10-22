package company.project.main.app.user

import java.time.Instant
import java.util.UUID
import org.springframework.stereotype.Service
import company.project.core.dto.app.user.*
import company.project.core.dto.common.user.UserCommonDto
import company.project.core.entity.UserEntity
import company.project.infra.repository.UserRepository
import company.project.lib.common.component.AuthComponent
import company.project.lib.common.enum.INTERNAL_ERROR_CODE
import company.project.lib.common.exception.ServerErrorException
import company.project.lib.common.util.Jwt
import company.project.lib.common.util.SHAType
import company.project.lib.common.util.Sha

@Service
class UserService(
	private val authComponent: AuthComponent,
	private val userRepository: UserRepository,
	private val sha: Sha,
	private val jwt: Jwt,
) {
	/**
	 * [UserService]
	 * - 프로필 조회(자신)
	 */
	fun getProfile(): UserCommonDto {
		val userTokenInfoDto = authComponent.getUserTokenInfo()

		// 프로필 조회 시 회원 정보 없으면 에러 처리
		val userEntity =
			kotlin
				.runCatching {
					userRepository.findByUid(userTokenInfoDto.uid)!!
				}.getOrElse {
					throw ServerErrorException(INTERNAL_ERROR_CODE.NOT_FOUND_USER)
				}

		return userEntity.mappingUserCommonDto()
	}

	/**
	 * [UserService]
	 * - 회원가입
	 */
	fun signUp(userSignUpInDto: UserSignUpInDto): UserCommonDto {
		// 이미 회원 가입한 회원인지 확인
		userRepository.findById(userSignUpInDto.id).let { userEntity ->
			userEntity?.let {
				throw ServerErrorException(INTERNAL_ERROR_CODE.ALREADY_EXIST_USER)
			}
		}

		// 회원가입 시 회원 정보 저장
		val userEntity =
			UserEntity(
				uid = UUID.randomUUID().toString(),
				id = userSignUpInDto.id,
				pw = sha.encrypt(userSignUpInDto.pw, SHAType.SHA256),
				email = userSignUpInDto.email,
				name = userSignUpInDto.name,
				createdAt = Instant.now(),
			).let { userEntity ->
				userRepository.save(userEntity)
			}

		return userEntity.mappingUserCommonDto()
	}

	/**
	 * [UserService]
	 * - 로그인
	 */
	fun login(userLoginInDto: UserLoginInDto): UserLoginOutDto {
		val userEntity = userRepository.findById(userLoginInDto.id) ?: throw ServerErrorException(INTERNAL_ERROR_CODE.NOT_FOUND_USER)
		val userCommonDto = userEntity.mappingUserCommonDto()

		if (userEntity.pw != sha.encrypt(userLoginInDto.pw, SHAType.SHA256)) {
			throw ServerErrorException(INTERNAL_ERROR_CODE.WRONG_PW)
		}

		val accessToken = jwt.createAccessToken(userCommonDto)
		val refreshToken = jwt.createRefreshToken(userCommonDto)

		return UserLoginOutDto(
			accessToken = accessToken,
			refreshToken = refreshToken,
		)
	}

	fun refreshToken(userRefreshTokenInDto: UserRefreshTokenInDto): UserRefreshTokenOutDto {
		val accessToken = jwt.refreshToken(userRefreshTokenInDto.refreshToken)

		return UserRefreshTokenOutDto(
			accessToken = accessToken,
		)
	}
}
