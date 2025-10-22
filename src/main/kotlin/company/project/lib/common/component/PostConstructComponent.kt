package company.project.lib.common.component

import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import company.project.lib.common.util.Etc

@Component
class PostConstructComponent(
	private val etc: Etc,
) {
	@PostConstruct
	fun init() {
		etc.initIp()
	}
}
