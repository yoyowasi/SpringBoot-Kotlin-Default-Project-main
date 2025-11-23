package company.project.core.dto.app.festival.response

import java.time.LocalDate
import java.time.LocalDateTime

data class JobResponse(
	val jobId: Long,
	val festivalId: Long,
	var employerUid: String,
	var employerName: String,
	val title: String,
	val shortDesc: String?,
	val detailDesc: String?,
	val hourlyPay: Int?,
	val workTime: String?,
	val workPeriod: String?,
	val preference: List<String>,
	val isCertified: Boolean,
	val isOpen: Boolean,
	val deadline: LocalDate?,
	val applicantCount: Int,
	val hiredCount: Int,
	var alreadyApplied: Boolean?=false,
	val createdAt: LocalDateTime?=null,
	val updatedAt: LocalDateTime?=null,
)
