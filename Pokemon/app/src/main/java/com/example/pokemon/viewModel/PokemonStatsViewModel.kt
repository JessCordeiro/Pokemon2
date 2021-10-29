package com.example.pokemon.viewModel

import androidx.lifecycle.ViewModel
import com.example.pokemon.data.repository.PokemonRepository
import com.example.pokemon.extractId
import com.example.pokemon.resource.NetworkResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class PokemonStatsViewModel @Inject constructor(private val pokemonRepository: PokemonRepository) :
    ViewModel() {


    suspend fun getSinglePokemon(url: String) = flow {
        val id = url.extractId()
        emit(NetworkResource.Loading)
        emit(pokemonRepository.getSinglePokemon(id))
    }



}
