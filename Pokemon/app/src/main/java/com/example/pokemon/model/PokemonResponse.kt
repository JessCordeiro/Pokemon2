package com.example.pokemon.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class PokemonResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    //val results: List<PokemonsApiResult>
): Parcelable


data class PagedResponse<T>(
    @SerializedName("info") val pageInfo: PokemonResponse,
    val results: List<PokemonsApiResult> = listOf()
)

