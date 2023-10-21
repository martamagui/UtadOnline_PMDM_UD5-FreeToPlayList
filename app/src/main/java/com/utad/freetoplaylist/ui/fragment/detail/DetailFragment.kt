package com.utad.freetoplaylist.ui.fragment.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.utad.freetoplaylist.R
import com.utad.freetoplaylist.databinding.FragmentDetailBinding
import com.utad.freetoplaylist.data.network.FreeToPlayApi
import com.utad.freetoplaylist.data.network.model.GameDetailResponse
import com.utad.freetoplaylist.utils.loadPicture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailFragment : Fragment() {

    private lateinit var _binding: FragmentDetailBinding
    private val binding: FragmentDetailBinding get() = _binding

    //Guardamos los argumentos de nuestra navegación para recuperar el id del juego clicado
    private val args: DetailFragmentArgs by navArgs()

    //Declaramos nuestro ViewModel
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        //Recuperamos de los argumentos de navegación del NavigationComponent el id del viedeojuego
        //y llamamos a la llamada en el viewModel
        viewModel.getGameDetail(args.id)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.detailUiState.collect { state ->
                if (state.isLoading) {
                    binding.pbDetail.visibility = View.VISIBLE
                } else {
                    binding.pbDetail.visibility = View.GONE
                }
                if (state.detailData != null) {
                    showGameData(state.detailData)
                }
                if (state.errorMessage != null) {
                    showRequestError(state.errorMessage)
                }
            }
        }
    }

    //region --- UI
    private fun showRequestError(errorCode: String) {
        val message = getString(R.string.detail_request_error_message, errorCode)
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showGameData(body: GameDetailResponse) {
        binding.ivGameDetail.loadPicture(body.thumbnail)
        binding.tvGameNameDetail.text = body.title
        binding.tvGameGenreDetail.text = body.genre
        binding.tvGamePlatformDetail.text = body.platform
        binding.tvGamePublisherDetail.text = body.publisher
        binding.tvGameDescriptionDetail.text = body.description
    }
    //endregion --- UI


}