package company.project.lib.util

import java.util.*
import net.datafaker.Faker
import company.project.core.entity.UserEntity
import company.project.lib.common.util.Random

class FakeUser {
	fun makeFakeUser(): UserEntity {
		val faker = Faker()
		val uid = UUID.randomUUID().toString()
		val referralCode = Random().generateRandomBytes(10)
		val email = faker.internet().emailAddress()
		val name = faker.name().name()
		val nickName = faker.funnyName().name()
		val birth = faker.date().birthday().toLocalDateTime().toLocalDate()
		val phoneNumber = faker.phoneNumber().cellPhone()
		val providerName = faker.options().option("email", "google", "kakao")
		val termsOfService = faker.bool().bool()
		val privacyPolicy = faker.bool().bool()
		val alertPolicy = faker.bool().bool()
		return UserEntity(
			uid = uid,
			referralCode = referralCode,
			email = email,
			name = name,
			nickName = nickName,
			birth = birth,
			phoneNumber = phoneNumber,
			providerName = providerName,
			termsOfService = termsOfService,
			privacyPolicy = privacyPolicy,
			alertPolicy = alertPolicy,
		)
	}
}
