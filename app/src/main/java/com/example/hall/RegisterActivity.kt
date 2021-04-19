package com.example.hall

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

@Suppress("DEPRECATION")
class RegisterActivity: AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var userName:EditText
    lateinit var email:EditText
    lateinit var password:EditText
    lateinit var register:Button
    lateinit var mProgress:ProgressBar
    lateinit var mProgressDialog:ProgressDialog
    lateinit var mDatabase: FirebaseDatabase
    lateinit var ref:DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)
        mAuth=FirebaseAuth.getInstance()
        mDatabase= FirebaseDatabase.getInstance()
        ref=mDatabase.reference.child("Users")
        userName=findViewById(R.id.InputName)
        email=findViewById(R.id.inputEmail)
        password=findViewById(R.id.inputPassword)
        register=findViewById(R.id.btn_register)
        mProgress= ProgressBar(this)
        mProgressDialog=ProgressDialog(this)
        register.setOnClickListener{
            val username = userName.text.toString().trim()
            val userEmail = email.text.toString().trim()
            val userPassword = password.text.toString().trim()

            if (TextUtils.isEmpty(username)){
                userName.error="Enter User Name"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(userEmail)){
                email.error="Enter Email"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(userPassword)){
                password.error="Enter User Name"
                return@setOnClickListener
            }
//
            if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                email.error="Enter Valid Email"
                return@setOnClickListener
            }
            if(userPassword.length<8){
                password.error="Password should contain 8 characters"
                return@setOnClickListener
            }
            registerUser(username,userEmail,userPassword)
        }
    }
    private fun registerUser(Username:String,UserEmail:String,UserPassword:String){
        mProgressDialog.setMessage("Loading Please Wait..")
        mProgressDialog.show()
        mAuth.createUserWithEmailAndPassword(UserEmail,UserPassword).addOnCompleteListener(this){task ->
            if(task.isSuccessful){
                val currentUser = mAuth.currentUser
                val currentUserdb= ref.child(currentUser?.uid!!)
                currentUserdb.child("Name").setValue(Username)
                currentUserdb.child("Email").setValue(UserEmail)



                Toast.makeText(this,"Successfully registered", Toast.LENGTH_LONG).show()
                val intent= Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
            }
            mProgressDialog.dismiss()
        }







    }



}