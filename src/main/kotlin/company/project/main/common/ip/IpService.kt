package company.project.main.common.ip

import org.springframework.stereotype.Service
import company.project.lib.common.util.Etc

@Service
class IpService(
	private val etc: Etc,
) {
	fun initIp() {
		etc.initIp()
	}
}
