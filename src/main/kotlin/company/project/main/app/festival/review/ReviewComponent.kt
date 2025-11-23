package company.project.main.app.festival.review

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

@Component
class ReviewComponent(
	private val stringRedisTemplate: StringRedisTemplate,

) {
	private fun key(reviewId: Long) = "review:like:count:$reviewId"

	fun getCount(festivalId: Long): Long? {
		return stringRedisTemplate.opsForValue()
			.get(key(festivalId))
			?.toLongOrNull()
	}
	fun incr(reviewId: Long): Long {
		return stringRedisTemplate.opsForValue().increment(key(reviewId)) ?: 0L
	}

	fun decr(reviewId: Long): Long {
		val current = stringRedisTemplate.opsForValue().decrement(key(reviewId)) ?: 0L
		// 음수 방지
		if (current < 0L) {
			stringRedisTemplate.opsForValue().set(key(reviewId), "0")
			return 0L
		}
		return current
	}
}
