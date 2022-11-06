package com.alkemy.alkemybankbase.ui.auth

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alkemy.alkemybankbase.databinding.FragmentLoginBinding
import com.alkemy.alkemybankbase.databinding.FragmentSignUpBinding

class LoginFragment : Fragment() {
    interface LoginFragmentActions{
        fun onRegisterButton()
        fun onLoginFieldValidate(email: String,password: String )

    }

    private lateinit var loginFragmentActions: LoginFragmentActions
    private lateinit var binding: FragmentLoginBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)

        loginFragmentActions = try{
            context as LoginFragmentActions
        }catch (e: ClassCastException){

            throw ClassCastException("$context must be implement LoginFragmentActivity")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         binding = FragmentLoginBinding.inflate(inflater)
        binding.loginRegisterButton.setOnClickListener{
            loginFragmentActions.onRegisterButton()
        }
        binding.loginButton.setOnClickListener{
            validarForm()
        }
        return binding.root
    }


    private fun validarForm(){

        binding.emailInput.error = ""
        binding.passwordInput.error = ""

        val email = binding.emailEdit.text.toString()

        if(!isvalidEmail(email)){
            binding.emailInput.error = "Email is not valid"
            return
        }
        val password = binding.passwordEdit.text.toString()
        if(password.isEmpty()){
            binding.passwordInput.error = "Password is not valid"
            return
        }




        loginFragmentActions.onLoginFieldValidate(email,password)
    }

    private fun isvalidEmail(email:String?):Boolean{
        return !email.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


}