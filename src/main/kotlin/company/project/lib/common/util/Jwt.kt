package company.project.lib.common.util

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import java.security.Key
import java.time.ZonedDateTime
import java.util.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import company.project.core.dto.common.basic.CustomTokenDto
import company.project.core.dto.common.user.UserCommonDto
import company.project.infra.repository.UserRepository
import company.project.lib.common.enum.INTERNAL_ERROR_CODE
import company.project.lib.common.exception.ServerErrorException

@Component
class Jwt(
	@Value("\${jwt.secret}") secretKey: String,
	@Value("\${jwt.access_token_expiration_time}") accessTokenExpTime: Long,
	@Value("\${jwt.refresh_token_expiration_time}") refreshTokenExpTime: Long,
	private val userRepository: UserRepository,
) {
	private var key: Key? = null
	private var accessTokenExpTime: Long = 0
	private var refreshTokenExpTime: Long = 0

	init {
		val keyBytes = Decoders.BASE64.decode(secretKey)
		this.key = Keys.hmacShaKeyFor(keyBytes)
		this.accessTokenExpTime = accessTokenExpTime
		this.refreshTokenExpTime = refreshTokenExpTime
	}

	/**
	 * Access Token 생성
	 * @param userCommonDto
	 * @return Access Token String
	 */
	fun createAccessToken(userCommonDto: UserCommonDto): String {
		return createToken(userCommonDto, accessTokenExpTime)
	}

	fun createRefreshToken(userCommonDto: UserCommonDto): String {
		return createRefreshToken(userCommonDto, refreshTokenExpTime)
	}

	/**
	 * JWT 생성
	 * @param userCommonDto
	 * @param expireTime
	 * @return JWT String
	 */
	private fun createToken(
		userCommonDto: UserCommonDto,
		expireTime: Long,
	): String {
		val claims = Jwts.claims()
		claims["uid"] = userCommonDto.uid
		claims["email"] = userCommonDto.email
		claims["name"] = userCommonDto.name

		val now: ZonedDateTime = ZonedDateTime.now()
		val tokenValidity: ZonedDateTime = now.plusSeconds(expireTime)

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(Date.from(now.toInstant()))
			.setExpiration(Date.from(tokenValidity.toInstant()))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact()
	}

	/**
	 * Token에서 User ID 추출
	 * @param token
	 * @return User ID
	 */
	fun getUserId(token: String?): Long {
		return parseClaims(token).get("id", Long::class.java)
	}

	/**
	 * JWT 검증
	 * @param token
	 * @return IsValidate
	 */
	fun validateToken(token: String?): CustomTokenDto {
		return kotlin.runCatching {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
			CustomTokenDto(
				uid = parseClaims(token).get("uid", String::class.java),
				email = parseClaims(token).get("email", String::class.java),
				name = parseClaims(token).get("name", String::class.java),
			)
		}.getOrElse {
			throw ServerErrorException(INTERNAL_ERROR_CODE.INVALID_TOKEN)
		}
	}

	/**
	 * JWT Claims 추출
	 * @param accessToken
	 * @return JWT Claims
	 */
	fun parseClaims(accessToken: String?): Claims {
		return try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).body
		} catch (e: ExpiredJwtException) {
			e.claims
		}
	}

	fun createRefreshToken(
		userCommonDto: UserCommonDto,
		expireTime: Long,
	): String {
		val claims = Jwts.claims()
		claims["uid"] = userCommonDto.uid

		val now: ZonedDateTime = ZonedDateTime.now()
		val tokenValidity: ZonedDateTime = now.plusSeconds(expireTime)

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(Date.from(now.toInstant()))
			.setExpiration(Date.from(tokenValidity.toInstant()))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact()
	}

	fun refreshToken(refreshToken: String): String {
		val refreshClaims = parseClaims(refreshToken)
		val uid = refreshClaims["uid", String::class.java]
		val userCommonDto = userRepository.findByUid(uid)!!.mappingUserCommonDto()

		val claims = Jwts.claims()
		claims["uid"] = userCommonDto.uid
		claims["email"] = userCommonDto.email
		claims["name"] = userCommonDto.name

		val now: ZonedDateTime = ZonedDateTime.now()
		val tokenValidity: ZonedDateTime = now.plusSeconds(accessTokenExpTime)

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(Date.from(now.toInstant()))
			.setExpiration(Date.from(tokenValidity.toInstant()))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact()
	}
}
