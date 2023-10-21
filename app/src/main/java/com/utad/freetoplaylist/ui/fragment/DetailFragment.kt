package com.utad.freetoplaylist.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.utad.freetoplaylist.R
import com.utad.freetoplaylist.databinding.FragmentDetailBinding
import com.utad.freetoplaylist.network.FreeToPlayApi
import com.utad.freetoplaylist.network.model.GameDetailResponse
import com.utad.freetoplaylist.utils.loadPicture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailFragment : Fragment() {

    private lateinit var _binding: FragmentDetailBinding
    private val binding: FragmentDetailBinding get() = _binding

    //Guardamos los argumentos de nuestra navegación para recuperar el id del juego clicado
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

        //Recuperamos de los argumentos de navegación del NavigationComponent el id del viedeojuego
        getGameDetail(args.id)
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

    //region --- Request Retrofit
    private fun getGameDetail(id: Int) {
        //Lanzamos una corrutina para llamar a el servicio del detalle
        lifecycleScope.launch(Dispatchers.IO) {
            //Llamamos al servicio del detalle
            val response = FreeToPlayApi.service.getGameDetails(id)
            //Si la llamada es exitosa y trae información en el body
            if (response.isSuccessful && response.body() != null) {
                //Accedemos al Hilo principal para pintar la respuesta
                withContext(Dispatchers.Main) {
                    showGameData(response.body()!!)
                }
            } else {
                //Si da error, accedemos al hilo principial y mostramos que hubo un error en la llamada
                Log.e(" DetailFrg", "Error: ${response.code()}, errorbody: ${response.errorBody()}")
                withContext(Dispatchers.Main) {
                    val errorCode = response.code()
                    showRequestError("$errorCode")
                }
            }
        }
    }
    //endregion --- Request Retrofit


}