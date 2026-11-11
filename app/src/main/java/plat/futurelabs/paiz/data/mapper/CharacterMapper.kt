package plat.futurelabs.paiz.data.mapper

import plat.futurelabs.paiz.Character
import plat.futurelabs.paiz.data.local.CharacterEntity

fun CharacterEntity.toModel(): Character{
    return Character(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender,
        image = image,
        isFavorite = isFavorite
    )
}

fun Character.toEntity(): CharacterEntity{
    return CharacterEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender,
        image = image,
        isFavorite = isFavorite
    )
}