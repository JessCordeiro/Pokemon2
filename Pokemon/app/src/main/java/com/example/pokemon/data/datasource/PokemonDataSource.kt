package com.example.pokemon.data.datasource

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pokemon.api.PokemonService
import com.example.pokemon.model.PokemonsApiResult
import com.example.pokemon.util.STARTING_OFFSET_INDEX
import java.io.IOException

class PokemonDataSource(private val pokemonService: PokemonService):
PagingSource<Int, PokemonsApiResult>()
{
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonsApiResult> {
        val offset = params.key ?: STARTING_OFFSET_INDEX


        return try {
            val response = pokemonService.listPokemons(20)
            val pagedResponse =  response.body()
            val data = pagedResponse?.results


            var nextPageNumber: Int? = null
            if(pagedResponse?.pageInfo?.next != null){
                val uri = Uri.parse(pagedResponse.pageInfo.next)
                val nextPageQuery = uri.getQueryParameter("limit")
                nextPageNumber = nextPageQuery?.toInt()
            }
            LoadResult.Page(
                data = data.orEmpty(),
                prevKey = null,
                nextKey = nextPageNumber
            )


        } catch (t: Throwable) {
            var exception = t

            if (t is IOException) {
                exception = IOException("Please check internet connection")
            }
            LoadResult.Error(exception)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, PokemonsApiResult>): Int? {

        return state.anchorPosition

    }
}