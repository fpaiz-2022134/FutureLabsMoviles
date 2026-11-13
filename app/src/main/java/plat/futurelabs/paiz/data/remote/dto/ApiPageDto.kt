package plat.futurelabs.paiz.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApiPageDto<T>(
    val info: ApiInfoDto,
    val results: List<T>
)

@Serializable
data class ApiInfoDto(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)