package company.project.main.app.festival.main

import company.project.core.dto.app.festival.request.FestivalReviewRequestDto
import company.project.core.dto.app.festival.response.FestivalLikeResponseDto
import company.project.core.dto.app.festival.response.FestivalListResponseDto
import company.project.core.dto.app.festival.response.FestivalReviewResponseDto
import company.project.core.entity.FestivalReview
import company.project.core.entity.UserFestivalLike
import company.project.infra.repository.FestivalRepository
import company.project.infra.repository.FestivalReviewRepository
import company.project.infra.repository.FestivalTagMapRepository
import company.project.infra.repository.UserFestivalLikeRepository
import company.project.lib.common.component.AuthComponent
import company.project.lib.common.enum.INTERNAL_ERROR_CODE
import company.project.lib.common.exception.ServerErrorException
import jakarta.transaction.Transactional
import java.time.Instant
import java.time.LocalDate
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class FestivalService(
	private val festivalRepository: FestivalRepository,
	val festivalTagMapRepository: FestivalTagMapRepository,
	private val authComponent: AuthComponent,
	private val userFestivalLikeRepository: UserFestivalLikeRepository,
	private val festivalReviewRepository: FestivalReviewRepository,
	private val festivalComponent: FestivalComponent
) {
	@Transactional
	fun getList(page: Int): List<FestivalListResponseDto> {
		val userTokenInfoDto = try {
			authComponent.getUserTokenInfo()
		}catch (e: ServerErrorException){ null }

		val today = LocalDate.now()

		// 페이지 번호가 1부터 온다고 가정하면 -1, 0부터면 그대로 사용
		val pageRequest = PageRequest.of(page - 1, 20)
		val festivals = festivalRepository.findByFestivalStartDateGreaterThanEqualOrderByFestivalStartDateAsc(today, pageRequest)

		return festivals.content.map { festival ->
			val festivalId = festival.id
			val category = festivalTagMapRepository.findAllByFestivalId(festivalId!!).map {
				it.tag?.category
			}.distinct()
			// 좋아요 여부
			val liked =
				userFestivalLikeRepository.existsByUserUidAndFestivalId(userTokenInfoDto?.uid, festivalId)
			festival.toDto().copy(
				like = liked,
				likeCount = festivalComponent.getCount(festivalId) ?: 0L,
				category = category
			)
		}
	}
	@Transactional
	fun getSearch(keyword: String): List<FestivalListResponseDto?>? {
		return festivalTagMapRepository.findByOriginalTokenLikeIgnoreCase(keyword)?.map {
			it.festival?.toDto()
		}
	}
	@Transactional
	fun toggleFestivalLike(festivalId: Long): FestivalLikeResponseDto {
		val user = authComponent.getUserTokenInfo()
		val festival = festivalRepository.findById(festivalId)
			.orElseThrow { ServerErrorException(INTERNAL_ERROR_CODE.FESTIVAL_NOT_FOUND) }

		val exists = userFestivalLikeRepository.existsByUserUidAndFestivalId(user.uid, festivalId)

		val newCount = if (!exists) {
			// Like 추가
			val like = UserFestivalLike().apply {
				this.festival = festival
				this.userUid = user.uid
				this.like = true
				this.createdAt = Instant.now()
			}
			userFestivalLikeRepository.save(like)
			festivalComponent.incr(festivalId)
		} else {
			val userFestivalLike = userFestivalLikeRepository.findByUserUidAndFestival_Id(user.uid, festivalId)
			if(userFestivalLike.like == true) { // 좋아요 취소
				userFestivalLike.like = false
				userFestivalLikeRepository.save(userFestivalLike)
				festivalComponent.decr(festivalId)
			}else{ // 좋아요 다시 누르기
				userFestivalLike.like = true
				userFestivalLikeRepository.save(userFestivalLike)
				festivalComponent.incr(festivalId)
			}
		}
		return FestivalLikeResponseDto(
			festivalId = festivalId,
			like = !exists,
			likeCount = newCount
		)
	}

}
