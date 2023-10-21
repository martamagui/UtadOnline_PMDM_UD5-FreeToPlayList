package com.utad.freetoplaylist.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.utad.freetoplaylist.R
import com.utad.freetoplaylist.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityHomeBinding
    private val binding: ActivityHomeBinding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }
}