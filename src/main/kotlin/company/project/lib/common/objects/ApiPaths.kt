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
	const val CPU_BURN = "$DEV/cpu-burn/{seconds}"
	const val GET_PW = "$DEV/get-pw/{pw}"

	// festival
	private const val FESTIVAL = "$APP/festival"
	const val FESTIVAL_LIST = "$FESTIVAL/list"
	const val FestIVAL_DETAIL = "$FESTIVAL/{festivalId}"
	const val FESTIVAL_SEARCH = "$FESTIVAL/{keyword}/search"
	const val FESTIVAL_TOGGLE_LIKE = "$FESTIVAL/{festivalId}/like"

	private const val FESTIVAL_REVIEW = "$FESTIVAL/{festivalId}/reviews"

	// review actions
	const val FESTIVAL_REVIEW_CREATE = FESTIVAL_REVIEW
	const val FESTIVAL_REVIEW_LIST_SELECTED = FESTIVAL_REVIEW

	const val FESTIVAL_REVIEW_LIST = "$FESTIVAL/reviews"
	const val FESTIVAL_REVIEW_DETAIL = "$FESTIVAL_REVIEW_LIST/{festivalReviewId}"

	// 리뷰 좋아요
	const val FESTIVAL_REVIEW_LIKE = "$FESTIVAL/reviews/{reviewId}/like"

	// 리뷰 댓글
	const val FESTIVAL_REVIEW_COMMENT = "$FESTIVAL/reviews/{reviewId}/comments"          // POST/GET
	const val FESTIVAL_REVIEW_COMMENT_DETAIL = "$APP/festival/review-comments/{commentId}" // PUT/DELETE

	private const val FESTIVAL_JOB = "$FESTIVAL/job"
	const val JOB = "$FESTIVAL_JOB/{jobId}"
	const val JOB_LIST = "$FESTIVAL_JOB/list"
	const val JOB_CREATE = "$FESTIVAL_JOB/{festivalId}/create"
	const val JOB_APPLY = "$JOB/apply"
	const val JOB_APPLY_ACCEPT = "$FESTIVAL_JOB/apply/{applyId}/accept"
	const val JOB_APPLICANTS = "$JOB/applicants"


	// user
	private const val USER = "$APP/user"
	const val PROFILE = "$USER/profile"
	const val SIGN_UP = "$USER/sign-up"
	const val LOGIN = "$USER/login"
	const val REFRESH_TOKEN = "$USER/refresh-token"

	// admin
	private const val ADMIN = "/admin"
}
