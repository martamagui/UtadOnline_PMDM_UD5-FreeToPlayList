package com.utad.freetoplaylist.ui.fragment.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utad.freetoplaylist.data.data_store.LocalStorageRepository
import com.utad.freetoplaylist.data.data_store.LocalStorageRepositoryImpl
import com.utad.freetoplaylist.data.firebase.AuthRepository
import com.utad.freetoplaylist.data.firebase.AuthRepositoryImpl
import com.utad.freetoplaylist.data.network.model.GameDetailResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUIState(
    val errorMessage: String?,
    val wasLoginSuccessfully: Boolean
)

class LoginViewModel : ViewModel() {

    private var _loginUiState: MutableStateFlow<LoginUIState> =
        MutableStateFlow(LoginUIState(null, false))
    val loginUiState: StateFlow<LoginUIState> get() = _loginUiState

    private val authRepository: AuthRepository = AuthRepositoryImpl()
    private lateinit var localStorageRepository: LocalStorageRepository

    fun checkIsUserLogged(context: Context) {
        if (this::localStorageRepository.isInitialized == false) {
            localStorageRepository = LocalStorageRepositoryImpl(context)
        }

        viewModelScope.launch(Dispatchers.IO) {
            val isLogged = localStorageRepository.isUserLogged()
            _loginUiState.update { it.copy(wasLoginSuccessfully = isLogged) }
        }
    }

    fun doLogin(email: String?, password: String?) {
        if (isDataValid(email, password)) {
            viewModelScope.launch(Dispatchers.IO) {
                val result = authRepository.signInFirebaseEmailAndPassword(email!!, password!!)
                if (result) {
                    //Si el login fue bien, lo guardamos en el storage y navegamos a la Home
                    localStorageRepository.setUserLogged(true)
                    _loginUiState.update { it.copy(wasLoginSuccessfully = true, errorMessage = null) }
                } else {
                    _loginUiState.update { it.copy(errorMessage = "Ha habido un error en el login") }
                }
            }
        } else {
            _loginUiState.update {
                it.copy(errorMessage = "Los campos no pueden estar vacíos")
            }
        }

    }

    private fun isDataValid(email: String?, password: String?): Boolean {
        return email?.isNotEmpty() == true && password?.isNotEmpty() == true
    }
}