package company.project.core.dto.app.user

data class UserSignUpInDto(
	val id: String,
	val pw: String,
	val email: String,
	val name: String,
	val nickName: String? = null,
	val phoneNumber: String? = null,
	val providerName: String? = null,
	val birth: String,
	val termsOfService: Boolean? = false,
	val privacyPolicy: Boolean? = false,
	val alertPolicy: Boolean? = false,
	val referralCodeReceived: String? = null,
)
