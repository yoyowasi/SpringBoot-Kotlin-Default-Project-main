package company.project.core.dto.app.festival.request

import java.time.LocalDate

data class JobCreateRequest(
	val title: String,
	val shortDesc: String?,
	val detailDesc: String?,
	val hourlyPay: Int?,
	val workTime: String?,
	val workPeriod: String?,
	val preference: List<String> = emptyList(), // 우대사항 목록
	val isCertified: Boolean = false,
	val deadline: LocalDate?
)
