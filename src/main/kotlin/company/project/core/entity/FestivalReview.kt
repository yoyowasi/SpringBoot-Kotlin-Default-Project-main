package company.project.core.entity

import company.project.core.dto.app.festival.response.FestivalReviewResponseDto
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(name = "festival_review", schema = "project")
open class FestivalReview {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	open var id: Long? = null

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "festival_id", nullable = false)
	open var festival: Festival? = null

	@NotNull
	@ColumnDefault("0")
	@Column(name = "rating", nullable = false)
	open var rating: Int? = null

	@NotNull
	@ColumnDefault("CURRENT_TIMESTAMP")
	@Column(name = "created_at", nullable = false)
	open var createdAt: Instant? = null

	@NotNull
	@ColumnDefault("CURRENT_TIMESTAMP")
	@Column(name = "updated_at", nullable = false)
	open var updatedAt: Instant? = null

	@Size(max = 1000)
	@Column(name = "content", length = 1000)
	open var content: String? = null

	@NotNull
	@Lob
	@Enumerated(EnumType.STRING)
	open var type: ReviewType? = null

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_uid", nullable = false, referencedColumnName = "uid")
	open var user: UserEntity? = null

	fun toResponse() = FestivalReviewResponseDto(
		id = this.id!!,
		userName = this.user?.name!!,
		rating = this.rating ?: 0,
		content = this.content,
		type = this.type!!,
		likeCount = 0L,
		liked = false,
		festivalId = this.festival!!.id!!,
		festivalName = this.festival!!.festivalName!!,
		comments = emptyList(),
		createdAt = this.createdAt!!.let { LocalDateTime.ofInstant(it, ZoneId.systemDefault()) },
		)
}
enum class ReviewType {
	REVIEW, TIP, MATE
}
