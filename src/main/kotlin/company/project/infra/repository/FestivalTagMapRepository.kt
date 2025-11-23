package company.project.infra.repository

import company.project.core.entity.FestivalTagMap
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FestivalTagMapRepository : JpaRepository<FestivalTagMap, Long> {

	@Query("select f from FestivalTagMap f where upper(f.originalToken) like upper(?1)")
	fun findByOriginalTokenLikeIgnoreCase(originalToken: String): List<FestivalTagMap>?

	fun existsByFestivalIdAndTagId(festivalId: Long, tagId: Long): Boolean

	fun findAllByFestivalId(festivalId: Long): List<FestivalTagMap>
}
