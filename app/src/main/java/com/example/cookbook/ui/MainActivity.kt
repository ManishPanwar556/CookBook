package com.example.cookbook.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cookbook.R
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    val auth = FirebaseAuth.getInstance().currentUser?.uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onStart() {
        super.onStart()
        if (auth != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, CuisineFragment()).commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, SignInFragment()).commit()
        }
    }

}