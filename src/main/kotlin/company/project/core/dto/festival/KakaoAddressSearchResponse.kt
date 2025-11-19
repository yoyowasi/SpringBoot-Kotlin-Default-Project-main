package company.project.core.dto.festival

data class KakaoAddressSearchResponse(
	val documents: List<KakaoAddressDocument>,
	val meta: KakaoAddressMeta
)

data class KakaoAddressDocument(
	val address: KakaoAddress?,
	val address_name: String?,
	val address_type: String?,
	val road_address: KakaoRoadAddress?,  // {} 일 수 있으므로 Null 허용
	val x: String?,
	val y: String?
)

data class KakaoAddress(
	val address_name: String?,
	val b_code: String?,
	val h_code: String?,
	val main_address_no: String?,
	val mountain_yn: String?,
	val region_1depth_name: String?,
	val region_2depth_name: String?,
	val region_3depth_h_name: String?,
	val region_3depth_name: String?,
	val sub_address_no: String?,
	val x: String?,
	val y: String?
)

data class KakaoRoadAddress(
	val address_name: String? = null,
	val region_1depth_name: String? = null,
	val region_2depth_name: String? = null,
	val region_3depth_name: String? = null,
	val road_name: String? = null,
	val underground_yn: String? = null,
	val main_building_no: String? = null,
	val sub_building_no: String? = null,
	val building_name: String? = null,
	val zone_no: String? = null,
	val x: String? = null,
	val y: String? = null
)

data class KakaoAddressMeta(
	val is_end: Boolean,
	val pageable_count: Int,
	val total_count: Int
)
