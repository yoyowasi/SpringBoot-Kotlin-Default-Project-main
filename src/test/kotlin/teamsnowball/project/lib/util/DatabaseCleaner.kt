package company.project.lib.util

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.transaction.Transactional
import javax.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class DatabaseCleaner(
	@PersistenceContext
	private val entityManager: EntityManager,
) {
	private val tableNames: MutableList<String> = ArrayList()

	@PostConstruct
	private fun findDatabaseTableNames() {
		val query = entityManager.createNativeQuery("SHOW TABLES")
		val tableInfos = query.resultList.map { it as? Array<*> }
		tableNames.addAll(tableInfos.filterIsInstance<Array<*>>().map { it[0].toString() })
	}

	private fun truncate() {
		entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate()
		for (tableName in tableNames) {
			entityManager.createNativeQuery("TRUNCATE TABLE $tableName").executeUpdate()
		}
		entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate()
	}

	@Transactional
	fun clear() {
		entityManager.clear()
		truncate()
	}
}
