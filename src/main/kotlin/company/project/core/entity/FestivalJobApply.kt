package company.project.core.entity

import company.project.core.dto.app.festival.response.JobApplyResponse
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.Instant
import java.time.LocalDateTime
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(name = "festival_job_apply", schema = "project")
open class FestivalJobApply {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	open var id: Long? = null

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "job_id", nullable = false)
	open var job: FestivalJob? = null

	@Size(max = 100)
	@NotNull
	@Column(name = "applicant_uid", nullable = false, length = 100)
	open var applicantUid: String? = null

	@Size(max = 100)
	@Column(name = "name", length = 100)
	open var name: String? = null

	@Size(max = 20)
	@Column(name = "gender", length = 20)
	open var gender: String? = null

	@Column(name = "age")
	open var age: Int? = null

	@Size(max = 200)
	@Column(name = "location", length = 200)
	open var location: String? = null

	@Lob
	@Column(name = "introduction", columnDefinition = "TEXT")
	open var introduction: String? = null

	@Lob
	@Column(name = "career", columnDefinition = "TEXT")
	open var career: String? = null

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	open var status: ApplyStatus = ApplyStatus.APPLIED

	@Column(name = "is_read")
	open var isRead: Boolean = false

	@ColumnDefault("CURRENT_TIMESTAMP")
	@Column(name = "created_at")
	open var createdAt: Instant? = null

	@ColumnDefault("CURRENT_TIMESTAMP")
	@Column(name = "updated_at")
	open var updatedAt: Instant? = null

	fun toResponse(): JobApplyResponse {
		return JobApplyResponse(
			applyId = this@FestivalJobApply.id!!,
			jobId = this@FestivalJobApply.job!!.id!!,
			applicantUid = this@FestivalJobApply.applicantUid!!,
			name = this@FestivalJobApply.name,
			gender = this@FestivalJobApply.gender,
			age = this@FestivalJobApply.age,
			location = this@FestivalJobApply.location,
			introduction = this@FestivalJobApply.introduction,
			career = this@FestivalJobApply.career,
			status = this@FestivalJobApply.status,
			isRead = this@FestivalJobApply.isRead,
			createdAt = LocalDateTime.ofInstant(this@FestivalJobApply.createdAt , java.time.ZoneId.systemDefault()),
			updatedAt = LocalDateTime.ofInstant(this@FestivalJobApply.updatedAt , java.time.ZoneId.systemDefault()),
		)
	}
}


enum class ApplyStatus {
	APPLIED, ACCEPTED, REJECTED, NONE
}
