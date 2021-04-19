package com.example.hall

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.google_dashboard.*

class GoogleDashboard :AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.google_dashboard)
        mAuth= FirebaseAuth.getInstance()

        val currentUser=mAuth.currentUser

        googelname.text = "Name: "+currentUser?.displayName
        googlemail.text="Email: "+currentUser?.email
        if (currentUser==null){
            val googleintent = Intent(this,MainActivity::class.java)
            startActivity(googleintent)
            finish()
        }
        Glide.with(this).load(currentUser?.photoUrl).into(profile_image)
        googlebtn_signout.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}