package com.utad.freetoplaylist.ui.fragment.login

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
import com.utad.freetoplaylist.R
import com.utad.freetoplaylist.data.data_store.LocalStorageRepositoryImpl
import com.utad.freetoplaylist.databinding.FragmentLoginBinding
import com.utad.freetoplaylist.data.firebase.AuthRepositoryImpl
import com.utad.freetoplaylist.ui.activity.HomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginFragment : Fragment() {

    private lateinit var _binding: FragmentLoginBinding
    private val binding: FragmentLoginBinding get() = _binding

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observerViewModel()
        viewModel.checkIsUserLogged(requireContext())

        setClicks()
    }

    private fun observerViewModel() {
        lifecycleScope.launch {
            viewModel.loginUiState.collect { state ->
                if (state.wasLoginSuccessfully) {
                    goToHome()
                }
                if (state.errorMessage != null) {
                    Toast.makeText(requireContext(), state.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //region --- UI
    private fun setClicks() {
        binding.btnLogin.setOnClickListener {
            doLogin()
        }
        binding.btnCreateAccocunt.setOnClickListener { goToSignUp() }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    //endregion --- UI

    //region --- Firebase
    private fun doLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        viewModel.doLogin(email, password)
    }
    //endregion --- Firebase

    //region --- Navegación
    private fun goToHome() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
        //Finalizamos esta Activity para que el usuario no pueda retroceder al login pulsando la flecha de retroceso
        requireActivity().finish()
    }

    private fun goToSignUp() {
        val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
        findNavController().navigate(action)
    }
    //endregion --- Navegación

    //region --- Otros

    //endregion --- Otros

}