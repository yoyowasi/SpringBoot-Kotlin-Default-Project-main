package company.project.main.app.festival.job

import company.project.core.dto.app.festival.request.JobApplyRequest
import company.project.core.dto.app.festival.request.JobCreateRequest
import company.project.core.dto.app.festival.response.JobApplyResponse
import company.project.core.dto.app.festival.response.JobResponse
import company.project.core.entity.ApplyStatus
import company.project.core.entity.FestivalJob
import company.project.core.entity.FestivalJobApply
import company.project.infra.repository.FestivalJobApplyRepository
import company.project.infra.repository.FestivalJobRepository
import company.project.infra.repository.FestivalRepository
import company.project.infra.repository.UserRepository
import company.project.lib.common.component.AuthComponent
import company.project.lib.common.enum.INTERNAL_ERROR_CODE
import company.project.lib.common.exception.ServerErrorException
import jakarta.transaction.Transactional
import java.time.Instant
import java.time.LocalDate
import org.springframework.stereotype.Service

@Service
class JobService(
	private val festivalRepository: FestivalRepository,
	private val authComponent: AuthComponent,
	private val festivalJobRepository: FestivalJobRepository,
	private val festivalJobApplyRepository: FestivalJobApplyRepository,
	private val userRepository: UserRepository
) {
	@Transactional
	fun createJob(festivalId: Long, request: JobCreateRequest): JobResponse {
		val festival = festivalRepository.findById(festivalId)
			.orElseThrow { ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_NOT_FOUND) }
		val user = authComponent.getUserTokenInfo()
		val job = FestivalJob().apply {
			this.festival = festival
			this.employerUid = user.uid
			this.title = request.title
			this.shortDesc = request.shortDesc
			this.detailDesc = request.detailDesc
			this.hourlyPay = request.hourlyPay
			this.workTime = request.workTime
			this.workPeriod = request.workPeriod
			this.preference = request.preference.joinToString(",")
			this.isCertified = request.isCertified
			this.deadline = request.deadline
			this.isOpen = true
			this.applicantCount = 0
			this.hiredCount = 0
			this.createdAt = Instant.now()
			this.updatedAt = Instant.now()
		}
		festivalJobRepository.save(job)
		return job.toResponse().copy(
			employerName = user.name?:"알 수 없음"
		)
	}
	@Transactional
	fun jobApply(jobId: Long, req: JobApplyRequest): JobApplyResponse {
		val user = authComponent.getUserTokenInfo()

		val job = festivalJobRepository.findById(jobId)
			.orElseThrow { ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_JOB_NOT_FOUND) }

		if (festivalJobApplyRepository.existsByJobIdAndApplicantUid(jobId, user.uid) == true) {
			throw ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_JOB_ALREADY_APPLIED)
		}

		val apply = FestivalJobApply().apply {
			this.job = job
			this.applicantUid = user.uid
			this.name = req.name
			this.gender = req.gender
			this.age = req.age
			this.location = req.location
			this.introduction = req.introduction
			this.career = req.career
			this.createdAt = Instant.now()
			this.updatedAt = Instant.now()
		}

		job.applicantCount = job.applicantCount?.plus(1)

		festivalJobApplyRepository.save(apply)
		festivalJobRepository.save(job)

		return apply.toResponse()
	}
	@Transactional
	fun cancelApply(jobId: Long): Boolean {
		val user = authComponent.getUserTokenInfo()

		val apply = festivalJobApplyRepository.findByJobIdAndApplicantUid(jobId, user.uid)
			?: throw ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_JOB_APPLY_NOT_FOUND)

		if (apply.isRead) throw ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_JOB_ALREADY_READ_BY_EMPLOYER)

		festivalJobApplyRepository.delete(apply)

		val job = apply.job!!
		job.applicantCount = job.applicantCount?.minus(1)

		return true
	}
	@Transactional
	fun updateJobApply(jobId: Long, req: JobApplyRequest): JobApplyResponse {
		val user = authComponent.getUserTokenInfo()

		val apply = festivalJobApplyRepository.findByJobIdAndApplicantUid(jobId, user.uid)
			?: throw ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_JOB_APPLY_NOT_FOUND)

		if (apply.isRead) throw ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_JOB_ALREADY_READ_BY_EMPLOYER)

		apply.apply {
			name = req.name
			gender = req.gender
			age = req.age
			location = req.location
			introduction = req.introduction
			career = req.career
			updatedAt = Instant.now()
		}

		return apply.toResponse()
	}
	@Transactional()
	fun getApplicants(jobId: Long): List<JobApplyResponse> {
		val user = authComponent.getUserTokenInfo()

		val job = festivalJobRepository.findById(jobId)
			.orElseThrow { ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_JOB_NOT_FOUND) }

		if (job.employerUid != user.uid) {
			throw ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_JOB_NOT_YOUR_JOB_POSTING)
		}

		val list = festivalJobApplyRepository.findAllByJobId(jobId)

		list.forEach { it.isRead = true } // once read → cannot modify/cancel

		return list.map { it.toResponse() }
	}
	@Transactional
	fun accept(applyId: Long): Boolean {
		val apply = festivalJobApplyRepository.findById(applyId)
			.orElseThrow { ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_JOB_APPLY_NOT_FOUND) }

		val employer = authComponent.getUserTokenInfo().uid

		if (apply.job!!.employerUid != employer)
			throw ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_JOB_NOY_YOUR_APPLICANT)

		apply.status = ApplyStatus.ACCEPTED

		apply.job!!.hiredCount = apply.job!!.hiredCount?.plus(1)

		return true
	}

	fun getUrgentJobs(): List<JobResponse> {
		val user = authComponent.getUserTokenInfoOrNull()

		return festivalJobRepository.findAllByDeadlineBetween(
			LocalDate.now(),
			LocalDate.now().plus(14, java.time.temporal.ChronoUnit.DAYS)
			// 2주 이내
		).sortedBy { it.deadline }.take(5).map {
			it.toResponse().apply {
				val alreadyApplied = festivalJobApplyRepository.existsByJobIdAndApplicantUid(it.id!!, user?.uid)
				this.alreadyApplied = alreadyApplied
				this.employerName = it.employerUid?.let { uid -> userRepository.findByUid(uid) }?.name ?: "알 수 없음"
			}
		}
	}

	@Transactional
	fun updateJob(jobId: Long, request: JobCreateRequest): JobResponse {
		val user = authComponent.getUserTokenInfo()
		val job = festivalJobRepository.findById(jobId)
			.orElseThrow { ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_JOB_NOT_FOUND) }
		if (job.employerUid != user.uid) {
			throw ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_JOB_NOT_YOUR_JOB_POSTING)
		}
		job.apply {
			this.title = request.title
			this.shortDesc = request.shortDesc
			this.detailDesc = request.detailDesc
			this.hourlyPay = request.hourlyPay
			this.workTime = request.workTime
			this.workPeriod = request.workPeriod
			this.preference = request.preference.joinToString(",")
			this.isCertified = request.isCertified
			this.deadline = request.deadline
			this.updatedAt = Instant.now()
		}
		festivalJobRepository.save(job)
		return job.toResponse().copy(
			employerName = user.name?:"알 수 없음"
		)
	}

	@Transactional
	fun deleteJob(jobId: Long): Boolean {
		val user = authComponent.getUserTokenInfo()
		val job = festivalJobRepository.findById(jobId)
			.orElseThrow { ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_JOB_NOT_FOUND) }
		if (job.employerUid != user.uid) {
			throw ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_JOB_NOT_YOUR_JOB_POSTING)
		}
		festivalJobRepository.delete(job)
		return true
	}

}
