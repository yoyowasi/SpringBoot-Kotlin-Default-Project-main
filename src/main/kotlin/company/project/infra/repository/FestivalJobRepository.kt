package company.project.infra.repository

import company.project.core.entity.FestivalJob
import java.time.LocalDate
import org.springframework.data.jpa.repository.JpaRepository

interface FestivalJobRepository : JpaRepository<FestivalJob, Long> {
	fun findAllByFestivalId(festivalId: Long): List<FestivalJob>

	fun findAllByDeadlineBetween(startDate: LocalDate, endDate: LocalDate): List<FestivalJob>
}
