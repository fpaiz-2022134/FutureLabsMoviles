package plat.futurelabs.paiz.data.mapper

import plat.futurelabs.paiz.Location
import plat.futurelabs.paiz.data.local.LocationEntity
import plat.futurelabs.paiz.data.remote.dto.LocationDto

fun LocationDto.toEntity() = LocationEntity(
    id = id,
    name = name,
    type = type,
    dimension = dimension,
    isFavorite = false
)

fun LocationEntity.toModel() = Location(
    id = id,
    name = name,
    type = type,
    dimension = dimension,
    isFavorite = isFavorite
)

fun Location.toEntity() = LocationEntity(
    id = id,
    name = name,
    type = type,
    dimension = dimension,
    isFavorite = isFavorite
)