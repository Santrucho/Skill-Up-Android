package com.Alkemy.alkemybankbase.ui.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.Alkemy.alkemybankbase.R
import com.Alkemy.alkemybankbase.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    private val GOOGLE_SIGN_IN = 100
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

            setup()
           // session()

        binding.btnSignUp.setOnClickListener{
            goSignUp()
        }




    }


//login
    private fun setup(){

        binding.btnLogin.setOnClickListener{
            if(binding.etEmail.text?.isNotEmpty() ?: false && binding.etPassword.text?.isNotEmpty() ?: false){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        showHome(it.result?.user?.email ?: "")
                    }else{
                        showAlert()
                    }
                }
            }
        }
    //Google Login
        binding.googleButton.setOnClickListener{
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent,GOOGLE_SIGN_IN)
        }
    }

    override fun onStart() {
        super.onStart()

        binding.root.visibility = View.VISIBLE
    }

    // Funcion para mantenerse logueado
    private fun session(){
        val pref: SharedPreferences = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE)
        val email = pref.getString("email",null)


        if (email != null){
            binding.root.visibility = View.INVISIBLE
            showHome(email)
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

    private fun showHome(email: String){
    val homeIntent: Intent = Intent(this,HomeActivity::class.java).apply{
        putExtra("email",email)
        }
        startActivity(homeIntent)
    }
    private fun goSignUp(){
        val signIntent: Intent = Intent(this, SignUpActivity::class.java)
        startActivity(signIntent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)

                if (account != null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken,null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener{
                        if (it.isSuccessful){
                            showHome(account.email ?: "")
                        }else{
                            showAlert()
                        }
                    }
                }
            }catch (e: ApiException){
                showAlert()
            }
        }

    }

}
