package com.utad.freetoplaywishlist.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.forEach
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.chip.ChipGroup.OnCheckedStateChangeListener
import com.utad.freetoplaywishlist.R
import com.utad.freetoplaywishlist.data_store.DataStoreManager
import com.utad.freetoplaywishlist.databinding.FragmentHomeBinding
import com.utad.freetoplaywishlist.firebase.authentification.AuthenticationManager
import com.utad.freetoplaywishlist.network.FreeToPlayApi
import com.utad.freetoplaywishlist.ui.activity.MainActivity
import com.utad.freetoplaywishlist.ui.adapter.GameListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    private val binding: FragmentHomeBinding get() = _binding

    //Adapter para nuestra RecyclerView
    private val adapter: GameListAdapter = GameListAdapter { id -> navigateToDetail(id) }

    //Creamos una variable que inicializaremos en el onCreate para el DataStoreManager
    private lateinit var dataStoreManager: DataStoreManager

    //AuthManager
    private val authManager: AuthenticationManager = AuthenticationManager()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataStoreManager = DataStoreManager(requireContext())
        setUI()
        getGamesRequest()
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
                getFilteredGames(current.text.toString())
            } else {
                getGamesRequest()
            }
        }

        binding.fabLogOut.setOnClickListener {
            logOut()
        }
    }
    //endregion --- UI

    //region --- Request
    private fun getGamesRequest() {
        binding.pbHome.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            val response = FreeToPlayApi.service.getGames()
            if (response.isSuccessful && response.body() != null
                && response.body()!!.isNotEmpty()
            ) {
                withContext(Dispatchers.Main) {
                    adapter.submitList(response.body())
                    binding.pbHome.visibility = View.GONE
                }
            } else {
                Log.e("HomeFrg", "Error: ${response.code()}, errorbody: ${response.errorBody()}")
                withContext(Dispatchers.Main) {
                    adapter.submitList(response.body())
                    binding.pbHome.visibility = View.GONE
                }
            }
        }
    }

    private fun getFilteredGames(category: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val response = FreeToPlayApi.service.getGamesByCategory(category)
            if (response.isSuccessful && response.body() != null
                && response.body()!!.isNotEmpty()
            ) {
                withContext(Dispatchers.Main) {
                    adapter.submitList(response.body())
                    binding.pbHome.visibility = View.GONE
                }
            } else {
                Log.e("HomeFrg", "Error: ${response.code()}, errorbody: ${response.errorBody()}")
                withContext(Dispatchers.Main) {
                    adapter.submitList(response.body())
                    binding.pbHome.visibility = View.GONE
                }
            }
        }
    }
    //endregion --- Request

    //region --- Firebase
    private fun logOut() {
        lifecycleScope.launch(Dispatchers.IO) {
            //Hacemos log out en el DataStore y firebase
            authManager.signOut()
            dataStoreManager.logOut()
            withContext(Dispatchers.Main) {
                goToLogin()
            }
        }
    }
    //endregion --- Firebase

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