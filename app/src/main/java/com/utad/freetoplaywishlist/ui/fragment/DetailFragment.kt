package com.utad.freetoplaywishlist.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.utad.freetoplaywishlist.R
import com.utad.freetoplaywishlist.databinding.FragmentDetailBinding
import com.utad.freetoplaywishlist.network.FreeToPlayApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailFragment : Fragment() {

    private lateinit var _binding: FragmentDetailBinding
    private val binding: FragmentDetailBinding get() = _binding

    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getGameDetail(args.id)
    }

    private fun getGameDetail(id: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            val response = FreeToPlayApi.service.getGameDetails(id)
            if (response.isSuccessful && response.body() != null) {
                Log.e(" DetailFrg", "Response: ${response.code()}, errorbody: ${response.body()}")
                withContext(Dispatchers.Main) {
                    //TODO pintar la vista
                }
            } else {
                Log.e(" DetailFrg", "Error: ${response.code()}, errorbody: ${response.errorBody()}")
                withContext(Dispatchers.Main) {
                    //todo
                }
            }
        }
    }
}