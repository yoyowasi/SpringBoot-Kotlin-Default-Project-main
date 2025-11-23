package company.project.core.dto.app.festival.request

data class JobApplyRequest(
	val name: String,
	val gender: String?,
	val age: Int?,
	val location: String?,
	val introduction: String?,
	val career: String?
)
