package com.example.pokemon.viewModel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pokemon.data.repository.PokemonRepository
import com.example.pokemon.model.PokemonsApiResult
//import com.example.pokemon.resource.NetworkResource
import com.example.pokemon.resource.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(private val pokemonRepository: PokemonRepository


): ViewModel() {



    //private val _pokemonFlow = MutableStateFlow<PagingData<PokemonsApiResult>>(PagingData.empty())
    private val  _pokemonFlow : MutableStateFlow<Resource<PagingData<PokemonsApiResult>>>? = null
   // private val  _pokemonFlow = Channel<Resource<PokemonsApiResult>>(Channel.BUFFERED)
   // val pokemonFlow : StateFlow<PagingData<PokemonsApiResult>> =  _pokemonFlow
    val pokemonFlow : StateFlow<Resource<PagingData<PokemonsApiResult>>>? = _pokemonFlow
  // val pokemonFlow  = _pokemonFlow.receiveAsFlow()
    //private var currentResult: Flow<PagingData<PokemonsApiResult>>? = null

      fun getPokemons(){
          viewModelScope.launch {
              try {
                      pokemonRepository.getPokemon().cachedIn(viewModelScope).collectLatest { _pokemonFlow?.value }

                 } catch (e : Exception){
                     _pokemonFlow?.emit(Resource.Failure(e))
                 }
          }
      }
    }






//fun getPokemons():Flow<PagingData<PokemonsApiResult>>?{
//
//    viewModelScope.launch {
//
//        //  pokemonRepository.getPokemon().collectLatest {
//
////                 _pokemonFlow?.emit(Resource.Loading())
////
//
//        pokemonRepository.getPokemon().collectLatest { _pokemonFlow?.value  }
//
//
////                 try {
////                      pokemonRepository.getPokemon().cachedIn(viewModelScope).collectLatest { _pokemonFlow?.value }
////
////                 } catch (e : Exception){
////                     _pokemonFlow?.emit(Resource.Failure(e))
////                 }
//
//
//    }
//    // pokemonRepository.getPokemon().collectLatest { _pokemonFlow.value = it }
//
//    return currentResult
//}

