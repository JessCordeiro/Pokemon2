package com.example.pokemon.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.pokemon.api.PokemonService
import com.example.pokemon.data.datasource.PokemonDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepository @Inject constructor(private val pokemonService: PokemonService) : BaseRepository(){

    fun getPokemon(searchString: String?) = Pager(
        config = PagingConfig(enablePlaceholders = false, pageSize= 25),
        pagingSourceFactory = {
            PokemonDataSource(pokemonService, searchString)
        }
    ).flow

    suspend fun getSinglePokemon(id: Int) = safeApiCall { pokemonService.getPokemon(id) }
}