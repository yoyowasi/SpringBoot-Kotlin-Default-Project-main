package company.project.main.scheduler

import org.springframework.stereotype.Service
import company.project.lib.common.annotation.Scheduler
import company.project.lib.common.util.Etc

@Service
class IpScheduler(
	private val etc: Etc,
) {
	@Scheduler(initialDelay = 0, fixedDelayString = "300000")
	fun initIp() {
		etc.initIp()
	}
}
