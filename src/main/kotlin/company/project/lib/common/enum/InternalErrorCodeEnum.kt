package company.project.lib.common.enum

import ch.qos.logback.core.status.ErrorStatus
import org.springframework.http.HttpStatus
import company.project.lib.common.dto.ServerErrorResponseDto

@Suppress("ktlint:standard:class-naming")
enum class INTERNAL_ERROR_CODE(val code: Int, val message: String, val httpStatus: HttpStatus, val errorStatus: Int) {
	// Auth : 1 ~ 50
	AUTHORIZATION_NOT_FOUND(1, "Authorization 헤더가 존재하지 않음", HttpStatus.UNAUTHORIZED, ErrorStatus.WARN),
	INVALID_TOKEN(2, "유효하지 않은 토큰", HttpStatus.UNAUTHORIZED, ErrorStatus.WARN),
	NOT_ADMIN(3, "관리자 권한이 없음", HttpStatus.FORBIDDEN, ErrorStatus.WARN),
	NOT_ALLOW_IP(4, "허용되지 않은 IP", HttpStatus.FORBIDDEN, ErrorStatus.WARN),
	DENY_IP(5, "차단된 IP", HttpStatus.FORBIDDEN, ErrorStatus.WARN),
	WRONG_PW(6, "비밀번호가 틀림", HttpStatus.BAD_REQUEST, ErrorStatus.WARN),

	// User : 51 ~ 100
	ALREADY_EXIST_USER(51, "이미 존재하는 유저", HttpStatus.CONFLICT, ErrorStatus.WARN),
	ALREADY_EXIST_NICKNAME(52, "이미 존재하는 닉네임", HttpStatus.CONFLICT, ErrorStatus.WARN),
	NOT_FOUND_USER(53, "존재하지 않는 유저", HttpStatus.NOT_FOUND, ErrorStatus.WARN),

	// Database
	NOT_FOUND_REPO(9996, "존재하지 않는 Repository", HttpStatus.NOT_FOUND, ErrorStatus.WARN),
	NOT_FOUND_DATA(9998, "데이터가 존재하지 않음", HttpStatus.NOT_FOUND, ErrorStatus.WARN),
	DB_USER_LEVEL_LOCK_NOT_RELEASE(9999, "해당 유저 DB Lock", HttpStatus.LOCKED, ErrorStatus.ERROR),

	// Festival
	FESTIVAL_NOT_FOUND(150, "존재하지 않는 축제", HttpStatus.NOT_FOUND, ErrorStatus.WARN),

	// Festival Review
	FESTIVAL_REVIEW_OWNERSHIP_MISMATCH(151, "자신의 리뷰만 수정할 수 있습니다.", HttpStatus.FORBIDDEN, ErrorStatus.WARN),
	FESTIVAL_REVIEW_DELETE_OWNERSHIP_MISMATCH(152, "자신의 리뷰만 삭제할 수 있습니다.", HttpStatus.FORBIDDEN, ErrorStatus.WARN),
	FESTIVAL_REVIEW_NOT_FOUND(153, "리뷰를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, ErrorStatus.WARN),
	FESTIVAL_REVIEW_ALREADY_EXISTS(154, "이미 작성한 리뷰가 존재합니다.", HttpStatus.CONFLICT, ErrorStatus.WARN),


	//Festival JOB
	FESTIVAL_JOB_NOT_FOUND(160, "존재하지 않는 축제 알바", HttpStatus.NOT_FOUND, ErrorStatus.WARN),
	FESTIVAL_JOB_APPLY_OWNERSHIP_MISMATCH(161, "자신의 지원서만 확인할 수 있습니다.", HttpStatus.FORBIDDEN, ErrorStatus.WARN),
	FESTIVAL_JOB_ALREADY_APPLIED(162, "이미 지원한 알바입니다.", HttpStatus.CONFLICT, ErrorStatus.WARN),
	FESTIVAL_JOB_APPLY_NOT_FOUND(163, "존재하지 않는 지원서", HttpStatus.NOT_FOUND, ErrorStatus.WARN),
	FESTIVAL_JOB_ALREADY_READ_BY_EMPLOYER(164, "이미 사장님이 확인한 지원서입니다.", HttpStatus.CONFLICT, ErrorStatus.WARN),
	FESTIVAL_JOB_NOT_YOUR_JOB_POSTING(165, "자신의 알바 공고만 확인할 수 있습니다.", HttpStatus.FORBIDDEN, ErrorStatus.WARN),
	FESTIVAL_JOB_NOY_YOUR_APPLICANT(166, "지원자 본인이 아닙니다.", HttpStatus.FORBIDDEN, ErrorStatus.WARN),



	// Test
	TEST(10000, "테스트 에러", HttpStatus.INTERNAL_SERVER_ERROR, ErrorStatus.ERROR),
	;

	fun mappingServerErrorResponseDto(): ServerErrorResponseDto {
		return ServerErrorResponseDto(
			code = code,
			name = name,
			message = message,
		)
	}
}
