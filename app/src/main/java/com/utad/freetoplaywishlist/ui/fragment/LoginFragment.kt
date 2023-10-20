package com.utad.freetoplaywishlist.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.EmailAuthCredential
import com.utad.freetoplaywishlist.R
import com.utad.freetoplaywishlist.databinding.FragmentLoginBinding
import com.utad.freetoplaywishlist.firebase.authentification.AuthenticationManager
import com.utad.freetoplaywishlist.ui.activity.HomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private lateinit var _binding: FragmentLoginBinding
    private val binding: FragmentLoginBinding get() = _binding

    private val authManager: AuthenticationManager = AuthenticationManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClicks()
    }

    private fun setClicks() {
        binding.btnLogin.setOnClickListener {
            //doLogin()
            navigateHome()
        }
        binding.btnCreateAccocunt.setOnClickListener { goToSignUp() }
    }

    private fun doLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.toString().trim()

        if (isDataValid(email, password)) {
            lifecycleScope.launch(Dispatchers.IO) {
                val result = authManager.signInFirebaseEmailAndPassword(email, password)
                if (result) {
                    navigateHome()
                }
            }
        } else {
            showToast(getString(R.string.login_invalid_data))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateHome() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
    }

    private fun isDataValid(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    private fun goToSignUp() {
        val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
        findNavController().navigate(action)
    }

}