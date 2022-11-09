package com.Alkemy.alkemybankbase.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.Alkemy.alkemybankbase.data.model.Provider
import com.Alkemy.alkemybankbase.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setup()

    }

    private fun setup(){

        binding.btnSignUp.setOnClickListener{
            if(binding.etEmail.text?.isNotEmpty() ?: false && binding.etPassword.text?.isNotEmpty() ?: false){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        showHome(it.result?.user?.email ?: "", Provider.BASIC)
                    }else{
                        showAlert()
                    }
                }
            }
        }
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("se ha producido un error autenticando el usuario")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String, provider: Provider){
        val homeIntent: Intent = Intent(this,HomeActivity::class.java).apply{
            putExtra("email",email)
            putExtra("provider",provider.name)
        }
        startActivity(homeIntent)
    }
}