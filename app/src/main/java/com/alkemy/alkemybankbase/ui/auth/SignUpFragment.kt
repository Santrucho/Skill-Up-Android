package com.alkemy.alkemybankbase.ui.auth

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alkemy.alkemybankbase.databinding.FragmentMovementBinding
import com.alkemy.alkemybankbase.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {

   private lateinit var binding: FragmentSignUpBinding

    interface SignUpFragmentActions{
        fun onSignUpFieldValidate(email: String,password: String, passwordConfirmation:String)
    }

    private lateinit var signUpFragmentActions: SignUpFragmentActions

    override fun onAttach(context: Context) {
        super.onAttach(context)

        signUpFragmentActions = try{
            context as SignUpFragmentActions
        }catch (e: ClassCastException){

            throw ClassCastException("$context must be implement LoginFragmentActivity")
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
          binding =FragmentSignUpBinding.inflate(inflater)
         setupSignUpButton()
        return binding.root

    }
    private fun setupSignUpButton() {
        binding.signUpButton.setOnClickListener{
             validarForm()
        }
    }
    private fun validarForm(){

        binding.emailInput.error = ""
        binding.passwordInput.error = ""
        binding.confirmPasswordInput.error = ""

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

        val passwordConfirmation = binding.confirmPasswordEdit.text.toString()
        if(passwordConfirmation.isEmpty()){
            binding.confirmPasswordInput.error = "Password it o"
            return
        }

        if(password  != passwordConfirmation){
            binding.passwordInput.error ="Passwords its not equals"
            return
        }

        signUpFragmentActions.onSignUpFieldValidate(email,password,passwordConfirmation)
    }

    private fun isvalidEmail(email:String?):Boolean{
            return !email.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }




}