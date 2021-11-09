package com.example.pokemon.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pokemon.data.datasource.PokemonDataSource
import com.example.pokemon.data.repository.PokemonRepository
import com.example.pokemon.model.Pokemon
import com.example.pokemon.model.PokemonsApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(private val pokemonRepository: PokemonRepository


): ViewModel(){

    private var currentResult: Flow<PagingData<PokemonsApiResult>>? = null

     fun getPokemons():Flow<PagingData<PokemonsApiResult>>{
        val newResult: Flow<PagingData<PokemonsApiResult>> =
        pokemonRepository.getPokemon().cachedIn(viewModelScope)
        currentResult = newResult
        return newResult
         //viewModelScope.launch {  }
    }





}