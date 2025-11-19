package company.project.infra.repository

import company.project.core.entity.Festival
import org.springframework.data.jpa.repository.JpaRepository

interface FestivalRepository  : JpaRepository<Festival, Long> {
}
