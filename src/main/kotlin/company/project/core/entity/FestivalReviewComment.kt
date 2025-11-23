package company.project.core.entity

import company.project.core.dto.app.festival.response.ReviewCommentResponseDto
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.Instant
import java.time.LocalDateTime
import org.hibernate.annotations.ColumnDefault

@Entity
@Table(name = "festival_review_comment", schema = "project")
open class FestivalReviewComment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	open var id: Long? = null

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "review_id", nullable = false)
	open var review: FestivalReview? = null


	@Size(max = 500)
	@NotNull
	@Column(name = "content", nullable = false, length = 500)
	open var content: String? = null

	@NotNull
	@Column(name = "createdAt", nullable = false)
	open var createdAt: Instant? = null

	@NotNull
	@Column(name = "updatedAt", nullable = false)
	open var updatedAt: Instant? = null

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@ColumnDefault("''")
	@JoinColumn(name = "user_uid", nullable = false, referencedColumnName = "uid")
	open var user: UserEntity? = null

	fun toResponse(): ReviewCommentResponseDto {
		return ReviewCommentResponseDto(
			commentId = this.id!!,
			reviewId = this.review!!.id!!,
			userName = this.user?.name!!,
			content = this.content!!,
			createdAt = LocalDateTime.ofInstant(this.createdAt!!, java.time.ZoneId.systemDefault()),
			updatedAt = LocalDateTime.ofInstant(this.updatedAt!!, java.time.ZoneId.systemDefault())
		)
	}

}
