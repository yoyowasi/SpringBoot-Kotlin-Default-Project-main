package company.project.lib.common.handler

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException
import company.project.lib.common.component.AuthComponent
import company.project.lib.common.dto.InternalServerErrorResponseDto
import company.project.lib.common.dto.LoggerInputDto
import company.project.lib.common.dto.ServerErrorResponseDto
import company.project.lib.common.dto.UserTokenInfoDto
import company.project.lib.common.exception.ServerErrorException
import company.project.lib.common.util.Etc
import company.project.lib.common.util.Logger

@RestControllerAdvice
class GlobalServerErrorExceptionHandler(
	private val logger: Logger,
	private val authComponent: AuthComponent,
	private val etc: Etc,
) {
	/**
	 * [GlobalServerErrorExceptionHandler]
	 * - 예측 가능한 에러 처리 핸들러
	 */
	@ExceptionHandler(ServerErrorException::class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	fun serverErrorExceptionHandler(
		request: HttpServletRequest,
		serverErrorException: ServerErrorException,
	): ResponseEntity<ServerErrorResponseDto> {
		val isTest = request.getHeader("test") == "true"
		val headers = request.headerNames.toList().associateWith { request.getHeader(it) }
		val userTokenInfoDto = if(serverErrorException.httpStatus != HttpStatus.UNAUTHORIZED
			&& (etc.getActiveProfile() == "dev" || etc.getActiveProfile() == "prod")
		) {
			authComponent.getUserTokenInfo()
		} else {
			null
		}

		val loggerInputDto =
			LoggerInputDto(
				uid = userTokenInfoDto?.uid ?: "Unknown",
				name = userTokenInfoDto?.name ?: "Unknown",
				email = userTokenInfoDto?.email ?: "Unknown",
				fileName = serverErrorException.stackTrace[0].fileName,
				funcName = serverErrorException.stackTrace[0].methodName,
				code = serverErrorException.errorCode,
				errorName = serverErrorException.errorName,
				errorMessage = serverErrorException.errorMessage,
				message = serverErrorException.errorName + " (${serverErrorException.errorMessage})",
				method = request.method,
				path = etc.getRequestPath(request),
				body = etc.getRequestBody(request),
				ip = etc.getIp(request),
				referer = request.getHeader("Page") ?: request.getHeader("Referer"),
				headers = headers,
			)
		if (!isTest) {
			when (serverErrorException.errorStatus) {
				1 -> {
					logger.warn(loggerInputDto)
				}
				2 -> {
					logger.error(loggerInputDto)
				}
			}
		}
		return ResponseEntity
			.status(serverErrorException.httpStatus)
			.body(serverErrorException.mappingServerErrorResponseDto())
	}

	/**
	 * [GlobalServerErrorExceptionHandler]
	 * - 존재하지 않는 리소스에 접근했을 경우 핸들러
	 */
	@ExceptionHandler(NoResourceFoundException::class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	fun noResourceFoundExceptionHandler(
		request: HttpServletRequest,
		noResourceFoundException: NoResourceFoundException,
	): ResponseEntity<InternalServerErrorResponseDto> {
		val isTest = request.getHeader("test") == "true"
		val headers = request.headerNames.toList().associateWith { request.getHeader(it) }
		var userTokenInfoDto: UserTokenInfoDto? = null
		runCatching {
			if( etc.getActiveProfile() == "dev" || etc.getActiveProfile() == "prod") {
				authComponent.getUserTokenInfo()
			} else {
				null
			}
		}.onSuccess {
				userTokenInfoDto = it
			}
		val loggerInputDto =
			LoggerInputDto(
				uid = userTokenInfoDto?.uid ?: "Unknown",
				name = userTokenInfoDto?.name ?: "Unknown",
				email = userTokenInfoDto?.email ?: "Unknown",
				fileName = noResourceFoundException.stackTrace[0].fileName,
				funcName = noResourceFoundException.stackTrace[0].methodName,
				message = noResourceFoundException.message ?: "Unknown",
				errorName = "NoResourceFoundException",
				errorMessage = "존재하지 않는 페이지",
				code = 0,
				method = request.method,
				path = etc.getRequestPath(request),
				body = etc.getRequestBody(request),
				ip = etc.getIp(request),
				referer = request.getHeader("Page") ?: request.getHeader("Referer"),
				headers = headers,
			)
		if (!isTest) {
			logger.warn(loggerInputDto)
		}
		return ResponseEntity
			.status(HttpStatus.NOT_FOUND)
			.body(
				InternalServerErrorResponseDto(
					message = "존재하지 않는 페이지입니다.",
				),
			)
	}

	/**
	 * [GlobalServerErrorExceptionHandler]
	 * - HTTP 요청 방식이 잘못되었을 경우 핸들러
	 */
	@ExceptionHandler(HttpMessageNotReadableException::class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	fun httpMessageNotReadableExceptionHandler(
		request: HttpServletRequest,
		httpMessageNotReadableException: HttpMessageNotReadableException,
	): ResponseEntity<InternalServerErrorResponseDto> {
		val isTest = request.getHeader("test") == "true"
		val headers = request.headerNames.toList().associateWith { request.getHeader(it) }
		var userTokenInfoDto: UserTokenInfoDto? = null
		runCatching {
			if( etc.getActiveProfile() == "dev" || etc.getActiveProfile() == "prod") {
				authComponent.getUserTokenInfo()
			} else {
				null
			}
		}.onSuccess {
				userTokenInfoDto = it
			}

		val loggerInputDto =
			LoggerInputDto(
				uid = userTokenInfoDto?.uid ?: "Unknown",
				name = userTokenInfoDto?.name ?: "Unknown",
				email = userTokenInfoDto?.email ?: "Unknown",
				fileName = httpMessageNotReadableException.stackTrace[0].fileName,
				funcName = httpMessageNotReadableException.stackTrace[0].methodName,
				message = httpMessageNotReadableException.message ?: "Unknown",
				errorName = "HttpMessageNotReadableException",
				errorMessage = "잘못된 API 요청",
				code = 0,
				method = request.method,
				path = etc.getRequestPath(request),
				body = etc.getRequestBody(request),
				ip = etc.getIp(request),
				referer = request.getHeader("Page") ?: request.getHeader("Referer"),
				headers = headers,
			)
		if (!isTest) {
			logger.warn(loggerInputDto)
		}
		return ResponseEntity
			.status(HttpStatus.UNPROCESSABLE_ENTITY)
			.body(
				InternalServerErrorResponseDto(
					message = "잘못된 API 요청입니다. 형식을 확인 후 다시 시도해주세요.",
				),
			)
	}

	/**
	 * [GlobalServerErrorExceptionHandler]
	 * - 유효성 검사 실패했을 경우 핸들러
	 */
	@ExceptionHandler(MethodArgumentNotValidException::class)
	fun handleValidationExceptions(
		request: HttpServletRequest,
		methodArgumentNotValidException: MethodArgumentNotValidException,
	): ResponseEntity<InternalServerErrorResponseDto> {
		val isTest = request.getHeader("test") == "true"
		val headers = request.headerNames.toList().associateWith { request.getHeader(it) }
		var userTokenInfoDto: UserTokenInfoDto? = null
		runCatching {
			if( etc.getActiveProfile() == "dev" || etc.getActiveProfile() == "prod") {
				authComponent.getUserTokenInfo()
			} else {
				null
			}
		}.onSuccess {
				userTokenInfoDto = it
			}

		val errors =
			methodArgumentNotValidException.bindingResult.allErrors
				.joinToString(", ") {
					(it as FieldError).field + " " + it.defaultMessage
				}

		val loggerInputDto =
			LoggerInputDto(
				uid = userTokenInfoDto?.uid ?: "Unknown",
				name = userTokenInfoDto?.name ?: "Unknown",
				email = userTokenInfoDto?.email ?: "Unknown",
				fileName = methodArgumentNotValidException.stackTrace[0].fileName,
				funcName = methodArgumentNotValidException.stackTrace[0].methodName,
				message = errors,
				errorName = "MethodArgumentNotValidException",
				errorMessage = "유효성 검사 실패 ($errors)",
				code = 0,
				method = request.method,
				path = etc.getRequestPath(request),
				body = etc.getRequestBody(request),
				ip = etc.getIp(request),
				referer = request.getHeader("Page") ?: request.getHeader("Referer"),
				headers = headers,
			)
		if (!isTest) {
			logger.warn(loggerInputDto)
		}
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(
				InternalServerErrorResponseDto(
					message = errors,
				),
			)
	}

	/**
	 * [GlobalServerErrorExceptionHandler]
	 * - 예측 불가능한 에러가 발생했을 경우 핸들러
	 */
	@ExceptionHandler(Exception::class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	fun exceptionHandler(
		request: HttpServletRequest,
		exception: Exception,
	): ResponseEntity<InternalServerErrorResponseDto> {
		val isTest = request.getHeader("test") == "true"
		val headers = request.headerNames.toList().associateWith { request.getHeader(it) }
		var userTokenInfoDto: UserTokenInfoDto? = null
		exception.printStackTrace()
		runCatching {
			if( etc.getActiveProfile() == "dev" || etc.getActiveProfile() == "prod") {
				authComponent.getUserTokenInfo()
			} else {
				null
			}
		}.onSuccess {
				userTokenInfoDto = it
			}

		val loggerInputDto =
			LoggerInputDto(
				uid = userTokenInfoDto?.uid ?: "Unknown",
				name = userTokenInfoDto?.name ?: "Unknown",
				email = userTokenInfoDto?.email ?: "Unknown",
				fileName = exception.stackTrace[0].fileName,
				funcName = exception.stackTrace[0].methodName,
				message = exception.message ?: "Unknown",
				errorName = "Exception",
				errorMessage = exception.toString(),
				code = 0,
				method = request.method,
				path = etc.getRequestPath(request),
				body = etc.getRequestBody(request),
				ip = etc.getIp(request),
				referer = request.getHeader("Page") ?: request.getHeader("Referer"),
				headers = headers,
			)
		if (!isTest) {
			logger.error(loggerInputDto)
		}
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(
				InternalServerErrorResponseDto(
					message = "서버 에러가 발생했습니다.",
				),
			)
	}
}
