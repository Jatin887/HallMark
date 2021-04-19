package com.example.hall
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.splashscreen.*


@Suppress("DEPRECATION")
class SplashScreen:AppCompatActivity() {
    lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashscreen)
        val topAnim = AnimationUtils.loadAnimation(this,R.anim.top)
        val bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom)
        Logo.startAnimation(bottomAnim)
        airplane.startAnimation(topAnim)
        mAuth= FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        val timeout = 4000
        val homeIntent=Intent(this,MainActivity::class.java)

         Handler().postDelayed({
             startActivity(homeIntent)
        }, timeout.toLong())

    }

}