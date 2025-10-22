package company.project.lib.common.annotation

import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@RequestMapping
@Operation
annotation class Api(
	val path: String = "",
	val method: RequestMethod = RequestMethod.GET,
	val summary: String = "",
	val description: String = "",
)
