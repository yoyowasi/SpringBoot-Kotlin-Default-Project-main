package company.project.core.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.Instant
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(name = "user_festival_like", schema = "project")
open class UserFestivalLike {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	open var id: Long? = null

	@Size(max = 100)
	@NotNull
	@Column(name = "user_uid", nullable = false, length = 100)
	open var userUid: String? = null

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "festival_id", nullable = false)
	open var festival: Festival? = null

	@NotNull
	@ColumnDefault("CURRENT_TIMESTAMP")
	@Column(name = "created_at", nullable = false)
	open var createdAt: Instant? = null

	@NotNull
	@ColumnDefault("(0)")
	@Column(name = "`like`", nullable = false)
	open var like: Boolean? = false
}
