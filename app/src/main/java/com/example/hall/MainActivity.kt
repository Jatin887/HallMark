package com.example.hall

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private val RC_SIGN_IN: Int=120
    lateinit var userLogin:Button
    lateinit var gso:GoogleSignInOptions
    lateinit var googleSignInClient:GoogleSignInClient
    lateinit var mAuth: FirebaseAuth
    lateinit var newUserRegister:TextView
    lateinit var useremail:EditText
    lateinit var password:EditText
    lateinit var forgotPassword:TextView
    lateinit var googleBtn:ImageView
    lateinit var phoneBtn:ImageView
    lateinit var ref: DatabaseReference
    lateinit var mProgressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userLogin = findViewById(R.id.btn_login)
        newUserRegister = findViewById(R.id.goToRegister)
        useremail = findViewById(R.id.inEmail)
        password = findViewById(R.id.inPassword)
        forgotPassword = findViewById(R.id.ForgotPassword)
        googleBtn = findViewById(R.id.googleLogin)
        phoneBtn = findViewById(R.id.phoneBtn)
        mAuth = FirebaseAuth.getInstance()
//

        // Configure Google Sign In
         gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient=GoogleSignIn.getClient(this,gso)
        googleBtn.setOnClickListener{
            signIn()
        }
        phoneBtn.setOnClickListener{
            val intent =Intent(this,PhoneLogin::class.java)
            startActivity(intent)
            finish()
        }
        forgotPassword.setOnClickListener {
            val intent=Intent(this,ForgotPassword::class.java)
            startActivity(intent)
            finish()
        }

        newUserRegister.setOnClickListener {
            val startIntent = Intent(this, RegisterActivity::class.java)
            startActivity(startIntent)
        }

        mAuth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference("Users")
        mProgressDialog= ProgressDialog(this)
        val currentUser=mAuth.currentUser
        if(currentUser!=null){
            startActivity(Intent(this,UserInfo::class.java))
            finish()
        }
        userLogin.setOnClickListener{
            val  email=useremail.text.toString().trim()
            val pass=password.text.toString().trim()
            if(TextUtils.isEmpty(email)){
                useremail.error="Enter Email"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(pass)){
                password.error="Enter password"
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                useremail.error="Enter Valid Email"
                return@setOnClickListener
            }
            loginUser(email,pass)
        }
    }
    private fun loginUser(Email:String,Password:String){
        mProgressDialog.setMessage("Loading Please Wait..")
        mProgressDialog.show()
        mAuth.signInWithEmailAndPassword(Email, Password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,"Successfully login",Toast.LENGTH_LONG).show()
                    val intent= Intent(this,UserInfo::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Wrong Password",
                        Toast.LENGTH_SHORT).show()

                }
                mProgressDialog.dismiss()
            }

    }


    public override fun onStart(){
        super.onStart()
        val currentUser = mAuth.currentUser
        if(currentUser!=null){

            updateUI(currentUser)
        }
    }
    fun updateUI(currentUser: FirebaseUser?){

    }
    private fun forgotpassword(usermail:EditText){
        if (usermail.text.toString().trim().isEmpty()){
            return
        }
//
        if (!Patterns.EMAIL_ADDRESS.matcher(usermail.text.toString().trim()).matches()){
            return
        }
        mAuth.sendPasswordResetEmail(usermail.text.toString().trim()).addOnCompleteListener{task->
            if(task.isSuccessful){
                Toast.makeText(this,"Email sent",Toast.LENGTH_SHORT).show()
            }

        }
    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e)
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    Toast.makeText(this,"Signed In Successfully",Toast.LENGTH_SHORT).show()
                    val googleintent = Intent(this,GoogleDashboard::class.java)
                    startActivity(googleintent)
                    finish()
                    val user = mAuth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this,"Sign in Failed",Toast.LENGTH_SHORT).show()

                    updateUI(null)
                }
            }
    }

}


