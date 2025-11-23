package company.project.infra.repository

import company.project.core.entity.UserFestivalLike
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserFestivalLikeRepository : JpaRepository<UserFestivalLike, Long> {
	fun existsByUserUidAndFestivalId(userUid: String?, festivalId: Long): Boolean

	fun deleteByUserUidAndFestivalId(userUid: String, festivalId: Long)

	fun findAllByUserUid(userUid: String): List<UserFestivalLike>
	@Query(
		"""
        select fl.festival.id as festivalId, count(fl) as likeCount
        from UserFestivalLike fl
        group by fl.festival.id
        """
	)
	fun countLikeGroupByFestival(): List<FestivalLikeCountProjection>
	@Query(
		"""
        select fl.festival.id as festivalId, count(fl) as likeCount
        from UserFestivalLike fl
        where fl.festival.id in :festivalIds
        group by fl.festival.id
        """
	)
	fun countByFestivalIds(
		@Param("festivalIds") festivalIds: List<Long>
	): List<FestivalLikeCountProjection>


	fun findByUserUidAndFestival_Id(userUid: String, id: Long): UserFestivalLike
}
interface FestivalLikeCountProjection {
	fun getFestivalId(): Long
	fun getLikeCount(): Long
}
