package company.project.core.entity

import company.project.core.dto.app.festival.response.JobResponse
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import org.hibernate.annotations.ColumnDefault

@Entity
@Table(name = "festival_job", schema = "project")
open class FestivalJob {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	open var id: Long? = null

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "festival_id", nullable = false)
	open var festival: Festival? = null

	@Size(max = 100)
	@NotNull
	@Column(name = "employer_uid", nullable = false, length = 100)
	open var employerUid: String? = null

	@Size(max = 200)
	@NotNull
	@Column(name = "title", nullable = false, length = 200)
	open var title: String? = null

	@Size(max = 500)
	@Column(name = "short_desc", length = 500)
	open var shortDesc: String? = null

	@Column(name = "detail_desc", columnDefinition = "TEXT")
	open var detailDesc: String? = null

	@Column(name = "hourly_pay")
	open var hourlyPay: Int? = null

	@Size(max = 200)
	@Column(name = "work_time", length = 200)
	open var workTime: String? = null

	@Size(max = 200)
	@Column(name = "work_period", length = 200)
	open var workPeriod: String? = null

	@Lob
	@Column(name = "preference", columnDefinition = "TEXT")
	open var preference: String? = null

	@ColumnDefault("0")
	@Column(name = "is_certified")
	open var isCertified: Boolean? = null

	@ColumnDefault("1")
	@Column(name = "is_open")
	open var isOpen: Boolean? = null

	@Column(name = "deadline")
	open var deadline: LocalDate? = null

	@ColumnDefault("0")
	@Column(name = "applicant_count")
	open var applicantCount: Int? = null

	@ColumnDefault("0")
	@Column(name = "hired_count")
	open var hiredCount: Int? = null

	@ColumnDefault("CURRENT_TIMESTAMP")
	@Column(name = "created_at")
	open var createdAt: Instant? = null

	@ColumnDefault("CURRENT_TIMESTAMP")
	@Column(name = "updated_at")
	open var updatedAt: Instant? = null

	fun toResponse(): JobResponse{
		return JobResponse(
			jobId = this@FestivalJob.id!!,
			festivalId = this@FestivalJob.festival!!.id!!,
			employerName = "",
			employerUid = this@FestivalJob.employerUid!!,
			title = this@FestivalJob.title!!,
			shortDesc = this@FestivalJob.shortDesc,
			detailDesc = this@FestivalJob.detailDesc,
			hourlyPay = this@FestivalJob.hourlyPay,
			workTime = this@FestivalJob.workTime,
			workPeriod = this@FestivalJob.workPeriod,
			preference = (this@FestivalJob.preference ?: "").split(",")
				.filter { it.isNotBlank() },
			isCertified = this@FestivalJob.isCertified == true,
			isOpen = this@FestivalJob.isOpen == true,
			deadline = this@FestivalJob.deadline,
			applicantCount = this@FestivalJob.applicantCount ?: 0,
			hiredCount = this@FestivalJob.hiredCount ?: 0,
			alreadyApplied = false,
			createdAt = LocalDateTime.ofInstant(this@FestivalJob.createdAt, java.time.ZoneId.systemDefault()),
			updatedAt = LocalDateTime.ofInstant(this@FestivalJob.updatedAt,java.time.ZoneId.systemDefault()),
		)
	}
}
