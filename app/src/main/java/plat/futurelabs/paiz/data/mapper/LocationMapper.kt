package plat.futurelabs.paiz.data.mapper

import plat.futurelabs.paiz.Location
import plat.futurelabs.paiz.data.local.LocationEntity

fun LocationEntity.toModel(): Location {
    return Location(
        id = id,
        name = name,
        type = type,
        dimension = dimension
    )
}

fun Location.toEntity(): LocationEntity {
    return LocationEntity(
        id = id,
        name = name,
        type = type,
        dimension = dimension
    )
}