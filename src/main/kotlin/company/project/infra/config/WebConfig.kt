package company.project.infra.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import company.project.lib.common.component.AuthComponent
import company.project.lib.common.interceptor.LogInterceptor
import company.project.lib.common.util.Etc
import company.project.lib.common.util.Logger

@Configuration
class WebConfig
	@Autowired
	constructor(
		private val logger: Logger,
		private val authComponent: AuthComponent,
		private val etc: Etc,
	) : WebMvcConfigurer {
		override fun addInterceptors(registry: InterceptorRegistry) {
			// path need to log check
			val logCheckList = listOf("/**")
			// path don`t need to log check
			val logExceptionCheckList = listOf("/icons/**", "/css/**", "/js/**", "/plugins/**", "/images/**")

			registry.addInterceptor(LogInterceptor(logger, authComponent, etc))
				.addPathPatterns(logCheckList)
				.excludePathPatterns(logExceptionCheckList)
		}
	}
