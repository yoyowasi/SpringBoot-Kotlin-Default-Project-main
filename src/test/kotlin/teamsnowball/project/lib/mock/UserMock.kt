package company.project.lib.mock

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import company.project.core.entity.UserEntity
import company.project.infra.repository.UserRepository
import company.project.lib.util.FakeUser

@Component
class UserMock(
	@Autowired private val userRepository: UserRepository,
) {
	/**
	 * [UserMock]
	 * - 유저 생성
	 */
	fun makeMockUser(count: Int): List<UserEntity> {
		for (i in 1..count) {
			userRepository.save(
				FakeUser().makeFakeUser(),
			)
		}
		return userRepository.findAll()
	}
}
