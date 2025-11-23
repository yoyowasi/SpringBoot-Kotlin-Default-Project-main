package company.project.main.app.festival.main

import company.project.infra.repository.UserFestivalLikeRepository
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

@Component
class FestivalComponent (
	private val stringRedisTemplate: StringRedisTemplate,
	private val userFestivalLikeRepository: UserFestivalLikeRepository
){
	private fun key(festivalId: Long) = "festival:like:count:$festivalId"

	fun getCount(festivalId: Long): Long? {
		return stringRedisTemplate.opsForValue()
			.get(key(festivalId))
			?.toLongOrNull()
	}

	fun mGetCounts(festivalIds: List<Long>): Map<Long, Long> {
		if (festivalIds.isEmpty()) return emptyMap()

		val keys = festivalIds.map { key(it) }
		val values = stringRedisTemplate.opsForValue().multiGet(keys) ?: emptyList()

		return festivalIds.zip(values).associate { (id, value) ->
			id to (value?.toLongOrNull() ?: 0L)
		}
	}

	fun incr(festivalId: Long): Long {
		return stringRedisTemplate.opsForValue().increment(key(festivalId)) ?: 0L
	}

	fun decr(festivalId: Long): Long {
		val current = stringRedisTemplate.opsForValue().decrement(key(festivalId)) ?: 0L
		// 음수 방지
		if (current < 0L) {
			stringRedisTemplate.opsForValue().set(key(festivalId), "0")
			return 0L
		}
		return current
	}

	fun setCount(festivalId: Long, count: Long) {
		stringRedisTemplate.opsForValue().set(key(festivalId), count.toString())
	}
	private fun syncCountFromDb(festivalId: Long): Long {
		val count = userFestivalLikeRepository.countByFestivalIds(listOf(festivalId))
			?.firstOrNull()
			?.getLikeCount()
			?: 0L

		setCount(festivalId, count)
		return count
	}

}
