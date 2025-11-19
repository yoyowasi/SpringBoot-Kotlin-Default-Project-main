package company.project.infra.repository

import company.project.core.entity.FestivalTagMap
import company.project.core.entity.FestivalTagMaster
import org.springframework.data.jpa.repository.JpaRepository

interface FestivalTagMasterRepository : JpaRepository<FestivalTagMaster, Long> {
	fun findByDimensionAndCategoryAndKeyword(
		dimension: String,
		category: String,
		keyword: String
	): FestivalTagMaster?
}
