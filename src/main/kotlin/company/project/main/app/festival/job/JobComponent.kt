package company.project.main.app.festival.job

import kotlin.text.toLongOrNull
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

@Component
class JobComponent(
	private val stringRedisTemplate: StringRedisTemplate,
) {
	private fun key(reviewId: Long) = "job:applicant:count:$reviewId"

	fun getCount(jobId: Long): Long? {
		return stringRedisTemplate.opsForValue()
			.get(key(jobId))
			?.toLongOrNull()
	}
	fun incr(jobId: Long): Long {
		return stringRedisTemplate.opsForValue().increment(key(jobId)) ?: 0L
	}

	fun decr(jobId: Long): Long {
		val current = stringRedisTemplate.opsForValue().decrement(key(jobId)) ?: 0L
		// 음수 방지
		if (current < 0L) {
			stringRedisTemplate.opsForValue().set(key(jobId), "0")
			return 0L
		}
		return current
	}
}
