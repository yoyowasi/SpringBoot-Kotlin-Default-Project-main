package company.project.core.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.Instant
import org.hibernate.annotations.ColumnDefault

@Entity
@Table(name = "festival_review_like", schema = "project")
open class FestivalReviewLike {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	open var id: Long? = null

	@NotNull
	@Column(name = "review_id", nullable = false)
	open var reviewId: Long? = null

	@Size(max = 255)
	@NotNull
	@ColumnDefault("''")
	@Column(name = "user_uid", nullable = false)
	open var userUid: String? = null

	@NotNull
	@Column(name = "`like`")
	open var like: Boolean? = false

	@Column(name = "created_at")
	open var createdAt: Instant? = null
}
