package plat.futurelabs.paiz.data.mapper

import plat.futurelabs.paiz.Character
import plat.futurelabs.paiz.data.local.CharacterEntity
import plat.futurelabs.paiz.data.remote.dto.CharacterDto

fun CharacterDto.toEntity() = CharacterEntity(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    image = image,
    isFavorite = false
)

fun CharacterEntity.toModel() = Character(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    image = image,
    isFavorite = isFavorite
)

fun Character.toEntity() = CharacterEntity(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    image = image,
    isFavorite = isFavorite
)