package company.project.core.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(name = "festival_tag_master", schema = "project")
open class FestivalTagMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	open var id: Long? = null

	@Size(max = 30)
	@NotNull
	@Column(name = "dimension", nullable = false, length = 30)
	open var dimension: String? = null

	@Size(max = 50)
	@NotNull
	@Column(name = "category", nullable = false, length = 50)
	open var category: String? = null

	@Size(max = 100)
	@NotNull
	@Column(name = "keyword", nullable = false, length = 100)
	open var keyword: String? = null

	@Size(max = 100)
	@NotNull
	@Column(name = "display_name", nullable = false, length = 100)
	open var displayName: String? = null
}
