package com.utad.freetoplaywishlist.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.utad.freetoplaywishlist.R
import com.utad.freetoplaywishlist.databinding.FragmentSignUpBinding
import com.utad.freetoplaywishlist.firebase.authentification.AuthenticationManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpFragment: Fragment() {

    private lateinit var _binding: FragmentSignUpBinding
    private val binding: FragmentSignUpBinding get() = _binding

    private val authManager: AuthenticationManager = AuthenticationManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClicks()
    }

    //region --- UI
    private fun setClicks() {
        binding.btnLogin.setOnClickListener {
            doSignUp()
        }
    }
    //region --- UI


    //region --- Firebase
    private fun doSignUp() {
        val age = binding.etSignUpAge.text.toString().toInt()
        val email = binding.etSignUpEmail.text.toString()
        val password = binding.etSignUpPassword.text.toString()
        if (validateData(email, password, age)) {
            registerUser(email, password)
        } else {
            showMessage(getString(R.string.login_invalid_data))
        }
    }

    private fun registerUser(email: String, password: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val wasSuccess = authManager.createUserFirebaseEmailAndPassword(email, password)
            withContext(Dispatchers.Main) {
                if (wasSuccess) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.sign_up_success_firebase_message),
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().popBackStack()
                } else {
                    showMessage(getString(R.string.sign_up_error_firebase_message))
                }
            }
        }
    }
    //endregion --- Firebase


    //region --- Otros
    private fun validateData(email: String?, password: String?, age: Int?): Boolean {
        return email != null && email.isNotEmpty() && password != null && password.isNotEmpty() && age != null
    }

    private fun showMessage(message: String) {
        //Podéis leer la documentación de como funcionan los diálogos aquí : https://developer.android.com/guide/topics/ui/dialogs?hl=es-419#kotlin
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(message)
            .setNeutralButton(
                R.string.sign_up_error_dialog_button
            ) { dialog, which ->
                //No hacemos nada, solo cerramos el diálogo
            }
        builder.create().show()
    }
    //endregion --- Otros
}