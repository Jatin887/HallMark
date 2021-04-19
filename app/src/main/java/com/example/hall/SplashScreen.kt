package com.example.hall
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


@Suppress("DEPRECATION")
class SplashScreen:AppCompatActivity() {
    lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashscreen)
        val topAnim = AnimationUtils.loadAnimation(this,R.anim.top)
        val bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom)
        val logo=findViewById<TextView>(R.id.Logo)
        val logoImg=findViewById<ImageView>(R.id.LogoImg)
        logoImg.startAnimation(topAnim)
        logo.startAnimation(bottomAnim)

        mAuth= FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        val timeout = 4000
        val homeIntent=Intent(this,MainActivity::class.java)

         Handler().postDelayed({
             if (user!=null){
                 startActivity(Intent(this,GoogleDashboard::class.java))
                 finish()
             }
             else{
                 startActivity(homeIntent)
             }
        }, timeout.toLong())

    }

}