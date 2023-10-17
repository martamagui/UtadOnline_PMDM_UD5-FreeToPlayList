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
import com.utad.freetoplaywishlist.databinding.FragmentHomeBinding
import com.utad.freetoplaywishlist.network.FreeToPlayApi
import com.utad.freetoplaywishlist.ui.adapter.GameListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    private val binding: FragmentHomeBinding get() = _binding

    private val adapter: GameListAdapter = GameListAdapter { id -> navigateToDetail(id) }


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
        getGamesRequest()
    }

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

    private fun setUI() {
        binding.rvGames.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvGames.adapter = adapter

        binding.cgCategories.setOnCheckedChangeListener { group, checkedId ->
            val current = group.findViewById<Chip>(checkedId)
            getFilteredGames(current.text.toString())
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

    private fun navigateToDetail(id: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(id)
        findNavController().navigate(action)
    }
}