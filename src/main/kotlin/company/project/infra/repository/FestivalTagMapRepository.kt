package company.project.infra.repository

import company.project.core.entity.FestivalTagMap
import company.project.core.entity.FestivalTagMaster
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FestivalTagMapRepository : JpaRepository<FestivalTagMap, Long> {
	fun existsByFestivalIdAndTagId(festivalId: Long, tagId: Long): Boolean

	fun findAllByFestivalId(festivalId: Long): List<FestivalTagMap>
}
