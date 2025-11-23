package company.project.infra.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {
	@Bean
	fun stringRedisTemplate(factory: RedisConnectionFactory): StringRedisTemplate {
		return StringRedisTemplate(factory)
	}

	@Bean
	fun redisTemplate(factory: RedisConnectionFactory): RedisTemplate<String, Any> {
		val template = RedisTemplate<String, Any>()
		template.setConnectionFactory(factory)

		template.keySerializer = StringRedisSerializer()
		template.valueSerializer = GenericJackson2JsonRedisSerializer()

		return template
	}
}
