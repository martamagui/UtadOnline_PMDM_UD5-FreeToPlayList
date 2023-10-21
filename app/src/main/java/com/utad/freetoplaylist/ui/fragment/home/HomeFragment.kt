package com.utad.freetoplaylist.ui.fragment.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.utad.freetoplaylist.databinding.FragmentHomeBinding
import com.utad.freetoplaylist.ui.activity.MainActivity
import com.utad.freetoplaylist.ui.adapter.GameListAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    private val binding: FragmentHomeBinding get() = _binding

    //Adapter para nuestra RecyclerView
    private val adapter: GameListAdapter = GameListAdapter { id -> navigateToDetail(id) }

    //ViewModel
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUI()
        observeViewModelStates()
        viewModel.getGamesRequest()
    }

    private fun observeViewModelStatesWithLiveData() {
      /*
        //No hay que lanzar una corrutina
        viewModel.homeState.observe(viewLifecycleOwner){ state->
            //Tu código para actualizar la vista con los valores del estado
        }
       */
    }

    private fun observeViewModelStates() {
        lifecycleScope.launch {
            viewModel.homeState.collect { state ->
                if (state.isLoading) {
                    binding.pbHome.visibility = View.VISIBLE
                } else {
                    binding.pbHome.visibility = View.GONE
                }

                if (state.gameList != null) {
                    if (adapter != null) {
                        adapter.submitList(state.gameList)
                    }
                }

                if (state.errorMessage != null) {
                    Toast.makeText(requireContext(), state.errorMessage, Toast.LENGTH_SHORT).show()
                }

                if(state.isLoggedOut){
                    goToLogin()
                }

            }
        }

        lifecycleScope.launch {
            viewModel.isLoggedOut.collect{isLoggedOut->
                if(isLoggedOut){
                    goToLogin()
                }
            }
        }
    }

    //region --- UI
    private fun setUI() {
        binding.rvGames.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvGames.adapter = adapter

        binding.cgCategories.setOnCheckedChangeListener { group, checkedId ->
            //Volvemos a lo alto de la lista
            binding.rvGames.scrollTo(0, 0)
            val current = group.findViewById<Chip>(checkedId)
            //Si hay categoría seleccionada, llamamos a la request de juegos filtrados
            //si no a la llamada general
            if (current != null) {
                viewModel.getFilteredGames(current.text.toString())
            } else {
                viewModel.getGamesRequest()
            }
        }

        binding.fabLogOut.setOnClickListener {
            viewModel.logOut(requireContext())
        }
    }
    //endregion --- UI

    //region --- Navegación
    private fun navigateToDetail(id: Int) {
        //Pasamos el id del item pulsado
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(id)
        findNavController().navigate(action)
    }

    private fun goToLogin() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }
    //endregion --- Navegación

}