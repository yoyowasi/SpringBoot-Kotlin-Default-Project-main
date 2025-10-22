package company.project.core.dto.app.user

data class UserLoginOutDto(
	val accessToken: String,
	val refreshToken: String,
)
