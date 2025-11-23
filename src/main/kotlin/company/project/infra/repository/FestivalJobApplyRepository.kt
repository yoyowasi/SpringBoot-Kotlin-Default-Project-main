package company.project.infra.repository

import company.project.core.entity.FestivalJobApply
import org.springframework.data.jpa.repository.JpaRepository

interface FestivalJobApplyRepository : JpaRepository<FestivalJobApply, Long> {
	fun findAllByJobId(jobId: Long): List<FestivalJobApply>

	fun existsByJobIdAndApplicantUid(jobId: Long?, uid: String?): Boolean?

	fun findByJobIdAndApplicantUid(jobId: Long, uid: String): FestivalJobApply?
}
