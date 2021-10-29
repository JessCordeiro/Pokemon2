package com.example.pokemon.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonsApiResult(
    val name: String,
    val url: String
) : Parcelable



