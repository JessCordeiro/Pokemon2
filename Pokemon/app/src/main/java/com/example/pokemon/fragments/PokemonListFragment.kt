package com.example.pokemon.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemon.R
import com.example.pokemon.adapter.LoadingStateAdapter
import com.example.pokemon.adapter.PokemonListAdapter
import com.example.pokemon.databinding.FragmentPokemonListBinding
import com.example.pokemon.model.PokemonsApiResult
import com.example.pokemon.resource.Resource

import com.example.pokemon.toast
import com.example.pokemon.util.PRODUCT_VIEW_TYPE
import com.example.pokemon.viewModel.PokemonListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.pokemon.toggle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.job
import java.util.Arrays.toString
import java.util.Objects.toString


@AndroidEntryPoint
@SuppressLint("ClickableViewAccessibility")
class PokemonListFragment : Fragment(R.layout.fragment_pokemon_list){

    private var hasInitiatedInitialCall = false
    private val viewModel: PokemonListViewModel by viewModels()
    private lateinit var binding: FragmentPokemonListBinding
    private var job: Job? = null


    private val adapter =
        PokemonListAdapter {
                pokemonResult: PokemonsApiResult, dominantColor: Int, picture: String? ->
            navigate(
                pokemonResult,
                dominantColor,
                picture
            )
        }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPokemonListBinding.bind(view)

        //viewModel.getPokemons()
        setAdapter()
        setRefresh()
        observer()



        binding.scrollUp.setOnClickListener {
            lifecycleScope.launch {
                binding.pokemonList.scrollToPosition(0)
                delay(100)
                binding.scrollUp.toggle(false)
            }
        }

    }

    fun setRefresh(){
        binding.swipeRefreshLayout.setOnRefreshListener {
            observer()
        }
    }

//     private fun observer(){
//        lifecycleScope.launchWhenStarted {
//            viewModel.pokemonFlow?.collect {
//                adapter.submitData(it)
//            }
//
//        }
//    }

//    private fun observer(){
//       lifecycleScope.launchWhenStarted{
//            viewModel.getPokemons()?.collect(){
//                adapter.submitData(it)
//                when(it){
//                    is Resource.Success<*> ->{
//
//                    }
//                }
//            }
//
//        }
//    }

    private fun observer(){

        viewModel.getPokemons()
        lifecycleScope.launchWhenStarted{
            viewModel.pokemonFlow?.collect(){
                when(it){
                     is Resource.Loading -> {
                        binding.progressCircular.isVisible = true
                    }

                    is Resource.Success -> {
                        binding.progressCircular.isVisible = false
                        adapter.submitData(it.data)

                    }

                    is Resource.Failure -> {
                        binding.progressCircular.isVisible = false
                        requireContext().toast("There was an error loading the pokemon")
                    }
                }

            }
        }

    }




    // private fun teste(){

           // job = lifecycleScope.launch{
            //    viewModel.getPokemons().collectLatest {
              //   adapter.submitData(it)
             //   }
           // }
       // }


    private fun setAdapter() {

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = adapter.getItemViewType(position)
                return if (viewType == PRODUCT_VIEW_TYPE) 1
                else 2
            }
        }
        binding.pokemonList.layoutManager = gridLayoutManager
        binding.pokemonList.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter { retry() }
        )

        binding.pokemonList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val scrolledPosition =
                    (recyclerView.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()

                if (scrolledPosition != null) {
                    if (scrolledPosition >= 1) {
                        binding.scrollUp.toggle(true)
                    } else {
                        binding.scrollUp.toggle(false)
                    }
                }

            }
        })

    hasInitiatedInitialCall = true


        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading && adapter.snapshot().isEmpty()
            ) {
                binding.progressCircular.isVisible = true
                binding.textError.isVisible = false


            } else {
                binding.progressCircular.isVisible = false
                binding.swipeRefreshLayout.isRefreshing = false



                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error

                    else -> null
                }
                if (adapter.snapshot().isEmpty()) {
                    error?.let {
                        binding.textError.visibility = View.VISIBLE
                        binding.textError.setOnClickListener {
                            adapter.retry()
                        }
                    }

                }
            }
        }

    }



    override fun onResume() {
        super.onResume()
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.white)
    }

    private fun retry() {
        adapter.retry()
    }


    private fun navigate(pokemonResult: PokemonsApiResult, dominantColor: Int, picture: String?) {
        binding.root.findNavController()
            .navigate(
                PokemonListFragmentDirections.toPokemonStatsFragment(
                    pokemonResult,
                    dominantColor, picture
                )
            )
    }


}