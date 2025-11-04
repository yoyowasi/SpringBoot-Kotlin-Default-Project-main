package company.project.lib.util

import java.util.*
import net.datafaker.Faker
import company.project.core.entity.UserEntity

class FakeUser {
	fun makeFakeUser(): UserEntity {
		val faker = Faker()
		val uid = UUID.randomUUID().toString()
		val email = faker.internet().emailAddress()
		val name = faker.name().name()

		return UserEntity(
			uid = uid,
			email = email,
			name = name,
		)
	}
}
