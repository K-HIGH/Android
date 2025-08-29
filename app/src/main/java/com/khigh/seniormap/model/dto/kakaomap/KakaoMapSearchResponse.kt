package com.khigh.seniormap.model.dto.kakaomap

import com.kakao.vectormap.LatLng
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

/**
 * 카카오맵 검색 API 응답 DTO
 * 카카오맵 로컬 API v2 응답 구조에 맞춤
 */
@Serializable
data class KakaoMapSearchResponse(
    val documents: List<KakaoMapDocument>,
    val meta: KakaoMapMeta
)

/**
 * 카카오맵 검색 결과 문서 DTO
 * 카카오맵 API 응답의 documents 배열 요소
 */
@Serializable
data class KakaoMapDocument(
    val id: String,
    @SerialName("place_name")
    val placeName: String,
    @SerialName("address_name")
    val addressName: String,
    @SerialName("road_address_name")
    val roadAddressName: String? = null,
    @SerialName("place_url")
    val placeUrl: String,
    @SerialName("category_name")
    val categoryName: String,
    @SerialName("category_group_code")
    val categoryGroupCode: String,
    @SerialName("category_group_name")
    val categoryGroupName: String,
    val phone: String? = null,
    val x: String, // 경도 (longitude)
    val y: String, // 위도 (latitude)
    val distance: String? = null
) {
    /**
     * LatLng 객체로 변환
     */
    fun toLatLng(): LatLng {
        return LatLng.from(y.toDouble(), x.toDouble())
    }
    
    /**
     * PlaceSearchResult로 변환
     */
    fun toPlaceSearchResult(): PlaceSearchResult {
        return PlaceSearchResult(
            id = id,
            name = placeName,
            address = addressName,
            roadAddress = roadAddressName,
            latLng = toLatLng(),
            category = categoryName,
            distance = distance,
            phone = phone,
            categoryGroup = categoryGroupName,
            placeUrl = placeUrl
        )
    }
}

/**
 * 카카오맵 검색 메타데이터 DTO
 * 검색 결과의 메타 정보
 */
@Serializable
data class KakaoMapMeta(
    @SerialName("total_count")
    val totalCount: Int,
    @SerialName("pageable_count")
    val pageableCount: Int,
    @SerialName("is_end")
    val isEnd: Boolean,
    @SerialName("same_name")
    val sameName: KakaoMapSameName? = null
)

/**
 * 카카오맵 동일명 검색 정보 DTO
 * 동일한 이름의 장소가 여러 지역에 있을 때의 정보
 */
@Serializable
data class KakaoMapSameName(
    val region: List<String>,
    val keyword: String,
    @SerialName("selected_region")
    val selectedRegion: String
)

/**
 * 장소 검색 결과 데이터 클래스 (UI에서 사용)
 * 카카오맵 API 응답을 UI에 적합한 형태로 변환
 */
data class PlaceSearchResult(
    val id: String,
    val name: String,
    val address: String,
    val roadAddress: String?,
    val latLng: LatLng,
    val category: String?,
    val distance: String? = null,
    val phone: String? = null,
    val categoryGroup: String? = null,
    val placeUrl: String? = null
) 