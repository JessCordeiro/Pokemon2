package com.example.pokemon.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pokemon(
    val sprites: Sprites,
    val stats: List<Stats>,
    val height: Int,
    val weight: Int,
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonsApiResult>
): Parcelable
