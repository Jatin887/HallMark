package com.example.hall

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.phone_userprofile.*

class PhoneUser:AppCompatActivity() {
    lateinit var auth:FirebaseAuth
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.phone_userprofile)
        auth= FirebaseAuth.getInstance()
        var currentUser=auth.currentUser
        PhoneNo.text = "User Phone No: "+currentUser?.phoneNumber
        var phoneSignOut=findViewById<Button>(R.id.btn_signoutphone)

//        if(currentUser==null){
//            startActivity(Intent(this,MainActivity::class.java))
//            finish()
//        }

        phoneSignOut.setOnClickListener{
            auth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}