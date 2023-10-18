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
import com.utad.freetoplaywishlist.network.model.GameDetailResponse
import com.utad.freetoplaywishlist.utils.loadPicture
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
                    showGameData(response.body()!!)
                }
            } else {
                Log.e(" DetailFrg", "Error: ${response.code()}, errorbody: ${response.errorBody()}")
                withContext(Dispatchers.Main) {
                    //todo
                }
            }
        }
    }

    private fun showGameData(body: GameDetailResponse) {
        binding.ivGameDetail.loadPicture(body.thumbnail)
        binding.tvGameNameDetail.text = body.title
        binding.tvGameGenreDetail.text = body.genre
        binding.tvGamePlatformDetail.text = body.platform
        binding.tvGamePublisherDetail.text = body.publisher
        binding.tvGameDescriptionDetail.text = body.description
    }
}