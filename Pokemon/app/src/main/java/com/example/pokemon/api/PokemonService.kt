package com.example.pokemon.api


import com.example.pokemon.model.GetPokemonResponse
import com.example.pokemon.model.PagedResponse
import com.example.pokemon.model.PokemonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {

    @GET("pokemon/")
    suspend fun listPokemons(@Query("limit") limit:Int): Response<PagedResponse<PokemonResponse>>

    @GET("pokemon/{id}/")
    suspend fun getPokemon(@Path("id") id: Int) : GetPokemonResponse

}