package com.example.hall

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.phone_userprofile.*
import kotlinx.android.synthetic.main.userinfo.*


class UserInfo: AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var logout:Button
    lateinit var mDatabase: FirebaseDatabase
    lateinit var ref: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.userinfo)
        mAuth= FirebaseAuth.getInstance()
        mAuth=FirebaseAuth.getInstance()
        mDatabase= FirebaseDatabase.getInstance()
        ref=mDatabase.reference.child("Users")
        loadProfile()



        logout=findViewById(R.id.btn_signout)
        logout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            sentTologin()
        }
    }
    private fun sentTologin(){
        val intent=Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    @SuppressLint("SetTextI18n")
    private fun loadProfile(){
        val currentUser=mAuth.currentUser
        val userRefrence= ref.child(currentUser?.uid!!)


        btn_signoutphone.setOnClickListener{
            mAuth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        umail.text="Email: "+ currentUser.email
        userRefrence.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("NOT YET IMPLEMENTED")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                uName.text="Name: "+snapshot.child("Name").value.toString()
            }

        })
    }


}