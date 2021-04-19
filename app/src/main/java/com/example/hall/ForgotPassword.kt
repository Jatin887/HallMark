package com.example.hall

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPassword: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        btn_submit.setOnClickListener{
            val fpass=email.text.toString().trim{it <=' '}
            if(fpass.isEmpty()){
                Toast.makeText(this,"Enter Email id",Toast.LENGTH_SHORT).show()
            }
            else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(fpass).addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        Toast.makeText(this,"Email Sent",Toast.LENGTH_SHORT).show()
                        var intent= Intent(this,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}