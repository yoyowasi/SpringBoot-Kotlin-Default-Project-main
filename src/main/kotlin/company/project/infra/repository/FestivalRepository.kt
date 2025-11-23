package company.project.infra.repository

import company.project.core.entity.Festival
import java.awt.print.Pageable
import java.time.LocalDate
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface FestivalRepository  : JpaRepository<Festival, Long> {
	@Query("select f from Festival f where f.festivalName = ?1 and f.festivalStartDate = ?2")
	fun findByFestivalNameAndFestivalStartDate(festivalName: String?, festivalStartDate: LocalDate?): Festival?

	fun findByFestivalStartDateGreaterThanEqualOrderByFestivalStartDateAsc(
		date: LocalDate,
		pageable: PageRequest
	): Page<Festival>
}
