package company.project.lib.common.util

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import org.apache.commons.codec.binary.Hex
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class Sha {
	@Value("\${sha.key}")
	private lateinit var key: String

	fun encrypt(
		data: String,
		shaType: SHAType,
	): String {
		val shaHmac = Mac.getInstance(shaType.type)
		val secretKey = SecretKeySpec(key.toByteArray(), shaType.type)
		shaHmac.init(secretKey)
		return Hex.encodeHexString(shaHmac.doFinal(data.toByteArray()))
	}
}

enum class SHAType(val type: String) {
	SHA1("HmacSHA1"),
	SHA256("HmacSHA256"),
}
