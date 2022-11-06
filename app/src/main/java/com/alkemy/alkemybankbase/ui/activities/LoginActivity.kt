package com.alkemy.alkemybankbase.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.alkemy.alkemybankbase.R
import com.alkemy.alkemybankbase.ui.auth.LoginFragment
import com.alkemy.alkemybankbase.ui.auth.LoginFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

class LoginActivity : AppCompatActivity()  {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



    }


}