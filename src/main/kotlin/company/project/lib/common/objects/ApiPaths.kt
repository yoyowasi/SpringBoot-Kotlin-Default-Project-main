package company.project.lib.common.objects

object ApiPaths {
	// server
	const val LOCALHOST = "http://localhost:"

	// common
	private const val COMMON = "/common"

	// test
	const val HEALTH = "$COMMON/health"
	const val ERROR = "$COMMON/error"
	const val USER_LEVEL_LOCK = "$COMMON/user-level-lock"

	// ip
	private const val IP = "$COMMON/ip"
	const val INIT_IP = "$IP/init"

	// app
	private const val APP = "/app"

	// dev
	private const val DEV = "$APP/dev"
	const val GET_PW = "$DEV/get-pw/{pw}"

	// user
	private const val USER = "$APP/user"
	const val PROFILE = "$USER/profile1026"
	const val SIGN_UP = "$USER/sign-up"
	const val LOGIN = "$USER/login"
	const val REFRESH_TOKEN = "$USER/refresh-token"

	// admin
	private const val ADMIN = "/admin"
}
