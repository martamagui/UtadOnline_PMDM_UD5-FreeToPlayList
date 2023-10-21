package com.utad.freetoplaylist.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.utad.freetoplaylist.R
import com.utad.freetoplaylist.data.data_store.DataStoreManager
import com.utad.freetoplaylist.databinding.FragmentLoginBinding
import com.utad.freetoplaylist.data.firebase.AuthenticationManager
import com.utad.freetoplaylist.ui.activity.HomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginFragment : Fragment() {

    private lateinit var _binding: FragmentLoginBinding
    private val binding: FragmentLoginBinding get() = _binding

    //Instanciamos nuestro manager
    private val authManager: AuthenticationManager = AuthenticationManager()

    //Creamos una variable que inicializaremos en el onCreate para el DataStoreManager
    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataStoreManager = DataStoreManager(requireContext())

        //Si el user está ya logeado, vamos a la home
        lifecycleScope.launch(Dispatchers.IO) {
            if (dataStoreManager.isUserLogged()) {
                withContext(Dispatchers.Main) {
                    goToHome()
                }
            }
        }

        setClicks()
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

        if (isDataValid(email, password)) {
            //Dentro de una corrutina llamamamos al login
            lifecycleScope.launch(Dispatchers.IO) {
                val result = authManager.signInFirebaseEmailAndPassword(email, password)
                if (result) {
                    //Si el login fue bien, lo guardamos en el storage y navegamos a la Home
                    dataStoreManager.setUserLogged(true)
                    withContext(Dispatchers.Main) {
                        goToHome()
                    }
                } else {
                    showToast(getString(R.string.login_invalid_credentials))
                }
            }
        } else {
            showToast(getString(R.string.login_invalid_data))
        }
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
    private fun isDataValid(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }
    //endregion --- Otros

}