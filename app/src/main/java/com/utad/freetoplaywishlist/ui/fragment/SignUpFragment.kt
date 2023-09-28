package com.utad.freetoplaywishlist.ui.fragment

import android.app.backup.BackupAgent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.utad.freetoplaywishlist.R
import com.utad.freetoplaywishlist.databinding.FragmentSignUpBinding
import com.utad.freetoplaywishlist.firebase.authentification.AuthenticationManager

class SignUpFragment : Fragment() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setClicks()
    }

    private fun setClicks() {
        binding.btnLogin.setOnClickListener {
            doSignUp()
        }
    }

    private fun validateData(name: String, email: String, password: String, age: Int): Boolean {
        //todo
        return true
    }

    private fun doSignUp() {
        if (validateData("", "", "", 0)) {

        } else {

        }
    }

}