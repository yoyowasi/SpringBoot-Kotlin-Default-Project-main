package company.project.main.app.festival.job

import company.project.core.dto.app.festival.request.JobApplyRequest
import company.project.core.dto.app.festival.request.JobCreateRequest
import company.project.core.dto.app.festival.response.JobApplyResponse
import company.project.core.dto.app.festival.response.JobResponse
import company.project.core.entity.UserRole
import company.project.lib.common.annotation.Api
import company.project.lib.common.annotation.Auth
import company.project.lib.common.objects.ApiPaths
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Festival Job", description = "축제 알바")
@RestController
class JobController(
	private val jobService: JobService
) {
	@Api(
		path = ApiPaths.JOB_LIST,
		method = RequestMethod.GET,
		summary = "마감이 임박한 축제 알바 리스트",
		description = "마감이 임박한 축제 알바 리스트 입니다.",
	)
	fun getUrgentJobs(): ResponseEntity<List<JobResponse>> {
		val result = jobService.getUrgentJobs()
		return ResponseEntity.ok().body(result)
	}

	@Api(
		path = ApiPaths.JOB,
		method = RequestMethod.POST,
		summary = "축제 알바 생성",
		description = "축제 알바 생성 입니다.",
	)
	@Auth(role = UserRole.USER)
	fun createJob(
		@PathVariable festivalId: Long,
		@RequestBody request: JobCreateRequest
	): ResponseEntity<JobResponse?> {
		val result = jobService.createJob(festivalId, request)
		return ResponseEntity.ok().body(result)
	}

	@Api(
		path = ApiPaths.JOB_APPLY,
		method = RequestMethod.POST,
		summary = "축제 알바 지원",
		description = "축제 알바 지원 입니다.",
	)
	@Auth(role = UserRole.USER)
	fun applyJob(
		@PathVariable jobId: Long,
		@RequestBody request: JobApplyRequest
	): ResponseEntity<JobApplyResponse?> {
		val result = jobService.jobApply(jobId, request)
		return ResponseEntity.ok().body(result)
	}
	@Api(
		path = ApiPaths.JOB_APPLY,
		method = RequestMethod.PUT,
		summary = "축제 알바 지원 수정",
		description = "축제 알바 지원 수정 입니다.",
	)
	@Auth(role = UserRole.USER)
	fun updateJobApply(
		@PathVariable jobId: Long,
		@RequestBody request: JobApplyRequest
	): ResponseEntity<JobApplyResponse?> {
		val result = jobService.updateJobApply(jobId, request)
		return ResponseEntity.ok().body(result)
	}
	@Api(
		path = ApiPaths.JOB_APPLY,
		method = RequestMethod.DELETE,
		summary = "축제 알바 지원 취소",
		description = "축제 알바 지원 취소 입니다.",
	)
	@Auth(role = UserRole.USER)
	fun cancelJobApply(
		@PathVariable jobId: Long,
	): ResponseEntity<Boolean> {
		val result = jobService.cancelApply(jobId)
		return ResponseEntity.ok().body(result)
	}
	@Api(
		path = ApiPaths.JOB_APPLICANTS,
		method = RequestMethod.GET,
		summary = "축제 알바 지원자 리스트",
		description = "축제 알바 지원자 리스트 입니다.",
	)
	@Auth(role = UserRole.USER)
	fun getJobApplicants(
		@PathVariable jobId: Long,
	): ResponseEntity<List<JobApplyResponse>> {
		val result = jobService.getApplicants(jobId)
		return ResponseEntity.ok().body(result)
	}
	@Api(
		path = ApiPaths.JOB_APPLY_ACCEPT,
		method = RequestMethod.POST,
		summary = "축제 알바 지원자 수락",
		description = "축제 알바 지원자 수락 입니다.",
	)
	@Auth(role = UserRole.USER)
	fun acceptJobApplicant(
		@PathVariable applyId: Long,
	): ResponseEntity<Boolean> {
		val result = jobService.accept(applyId)
		return ResponseEntity.ok().body(result)
	}



}
