package com.utad.freetoplaylist.ui.fragment.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.utad.freetoplaylist.data.network.model.GameDetailResponse
import com.utad.freetoplaylist.data.network.repository.FreeToPlayRepository
import com.utad.freetoplaylist.data.network.repository.FreeToPlayRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailUIState(
    val detailData: GameDetailResponse?,
    val errorMessage: String?,
    val isLoading: Boolean
)

class DetailViewModel : ViewModel() {
    private var _detailUiState: MutableStateFlow<DetailUIState> =
        MutableStateFlow(DetailUIState(null, null, false))
    val detailUiState: StateFlow<DetailUIState> get() = _detailUiState

    //Instanciamos nuestro repositorio de la Api
    //Diciendo que es del tipo de la interfaz (FreeToPlayRepository)
    // y lo inicializamos con la clase de la implementación (FreeToPlayRepositoryIMPL)
    private val apiRepository: FreeToPlayRepository = FreeToPlayRepositoryImpl()


    //region --- Request Retrofit
    fun getGameDetail(id: Int) {
        _detailUiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val response = apiRepository.getGameDetails(id)
            if (response.isSuccessful && response.body() != null) {
                _detailUiState.update { it.copy(detailData = response.body(), isLoading = false) }
            } else {
                _detailUiState.update {
                    it.copy(
                        errorMessage = "Ha habido un error al cargar la información, código ${response.code()}",
                        isLoading = false
                    )
                }
            }

        }
    }
    //endregion --- Request Retrofit

}