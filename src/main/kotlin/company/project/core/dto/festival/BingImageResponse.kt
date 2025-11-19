package company.project.core.dto.festival

data class BingImageResponse(
	val value: List<BingImageResult>
)

data class BingImageResult(
	val contentUrl: String
)
