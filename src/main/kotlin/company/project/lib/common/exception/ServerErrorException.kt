package company.project.lib.common.exception

import company.project.lib.common.dto.ServerErrorResponseDto
import company.project.lib.common.enum.INTERNAL_ERROR_CODE

class ServerErrorException(internalErrorCode: INTERNAL_ERROR_CODE) : RuntimeException() {
	val errorCode = internalErrorCode.code
	val errorName = internalErrorCode.name
	val errorMessage = internalErrorCode.message
	val httpStatus = internalErrorCode.httpStatus
	val errorStatus = internalErrorCode.errorStatus

	fun mappingServerErrorResponseDto(): ServerErrorResponseDto {
		return ServerErrorResponseDto(
			code = errorCode,
			name = errorName,
			message = errorMessage,
		)
	}
}
