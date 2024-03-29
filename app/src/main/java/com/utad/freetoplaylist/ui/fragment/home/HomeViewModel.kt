package com.utad.freetoplaylist.ui.fragment.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utad.freetoplaylist.data.data_store.LocalStorageRepositoryImpl
import com.utad.freetoplaylist.data.firebase.AuthRepositoryImpl
import com.utad.freetoplaylist.data.network.FreeToPlayApi
import com.utad.freetoplaylist.data.network.model.GameResponse
import com.utad.freetoplaylist.data.network.repository.FreeToPlayRepository
import com.utad.freetoplaylist.data.network.repository.FreeToPlayRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class HomeUIState(
    val gameList: List<GameResponse>?,
    val errorMessage: String?,
    val isLoading: Boolean,
    val isLoggedOut: Boolean
)

class HomeViewModel : ViewModel() {

    private val apiRepository: FreeToPlayRepository = FreeToPlayRepositoryImpl()

    fun getGamesRequest() {
        _homeState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(Dispatchers.IO) {

            val response = apiRepository.getGames()

            if (response.isSuccessful && response.body() != null
                && response.body()!!.isNotEmpty()
            ) {
                //Actualizamos con la lista de juegos recibida
                _homeState.update {
                    it.copy(gameList = response.body(), isLoading = false, errorMessage = null)
                }
            } else {
                _homeState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Ha habido un error al cargar la petición código: ${response.code()}"
                    )
                }
            }
        }
    }




    //Instanciamos nuestro repositorio de la Api
    //Diciendo que es del tipo de la interfaz (FreeToPlayRepository)
    // y lo inicializamos con la clase de la implementación (FreeToPlayRepositoryIMPL)


    /*
    //--- Versión de LiveData
    private var _homeState: MutableLiveData<HomeUIState> = MutableLiveData(
        HomeUIState(null, null, false, false))
    val homeState: LiveData<HomeUIState> get() = _homeState

        // Para actualizar el estado dentro de una función
        val newState = HomeUIState(null,null, isLoading = true, false)
        _homeState.postValue(newState )
    */

    private var _homeState: MutableStateFlow<HomeUIState> =
        MutableStateFlow(HomeUIState(null, null, false, false))
    val homeState: StateFlow<HomeUIState> get() = _homeState

    private var _isLoggedOut: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoggedOut: StateFlow<Boolean> get() = _isLoggedOut

    //Creamos una variable que inicializaremos en logOut() para el DataStoreManager
    private lateinit var dataStoreManager: LocalStorageRepositoryImpl
    //AuthManager
    private val authManager: AuthRepositoryImpl = AuthRepositoryImpl()


    //region --- Peticiones Http



    fun getFilteredGames(category: String) {
        //Para indicar que está cargando la vista actualizamos el estado
        _homeState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch(Dispatchers.IO) {
            val response = FreeToPlayApi.service.getGamesByCategory(category)

            if (response.isSuccessful && response.body() != null
                && response.body()!!.isNotEmpty()
            ) {
                //Actualizamos con la lista de juegos recibida
                _homeState.update {
                    it.copy(gameList = response.body(), isLoading = false, errorMessage = null)
                }
            } else {
                _homeState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Ha habido un error al cargar la petición código: ${response.code()}"
                    )
                }
            }
        }
    }
    //endregion --- Peticiones Http

    //region --- Firebase/DataStore
    fun logOut(context: Context) {
        if(this::dataStoreManager.isInitialized == false){
            dataStoreManager = LocalStorageRepositoryImpl(context)
        }
        viewModelScope.launch(Dispatchers.IO){
            authManager.signOut()
            dataStoreManager.logOut()
            _isLoggedOut.update { true }
        }
    }

    //endregion --- Firebase/DataStore


}