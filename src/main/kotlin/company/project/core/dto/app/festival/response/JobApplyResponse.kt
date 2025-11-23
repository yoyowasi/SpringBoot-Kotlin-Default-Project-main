package company.project.core.dto.app.festival.response

import company.project.core.entity.ApplyStatus
import java.time.LocalDateTime

data class JobApplyResponse(
	val applyId: Long,
	val jobId: Long,
	val applicantUid: String,
	val name: String?,
	val gender: String?,
	val age: Int?,
	val location: String?,
	val introduction: String?,
	val career: String?,
	val status: ApplyStatus,     // APPLIED / ACCEPTED / REJECTED
	val isRead: Boolean,
	val createdAt: LocalDateTime?=null,
	val updatedAt: LocalDateTime?=null,
)
