package company.project.infra.repository

import company.project.core.entity.FestivalJob
import java.awt.print.Pageable
import java.time.LocalDate
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository

interface FestivalJobRepository : JpaRepository<FestivalJob, Long> {
	fun findAllByFestivalId(festivalId: Long): List<FestivalJob>

	fun findAllByDeadlineBetween(
		start: LocalDate,
		end: LocalDate,
		pageRequest: PageRequest
	): Page<FestivalJob>
}
