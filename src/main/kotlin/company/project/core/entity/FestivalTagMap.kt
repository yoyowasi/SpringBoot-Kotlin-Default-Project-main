package company.project.core.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Size
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(name = "festival_tag_map", schema = "project")
open class FestivalTagMap {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	open var id: Long? = null

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "festival_id", nullable = false)
	open var festival: Festival? = null

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "tag_id", nullable = false)
	open var tag: FestivalTagMaster? = null

	@Size(max = 200)
	@Column(name = "original_token", length = 200)
	open var originalToken: String? = null
}
