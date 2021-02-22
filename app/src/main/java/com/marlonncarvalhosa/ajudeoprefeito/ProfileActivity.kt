package com.marlonncarvalhosa.ajudeoprefeito

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*


class ProfileActivity : AppCompatActivity() {
    var firebaseAuth: FirebaseAuth? = null
    private var user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        firebaseAuth = FirebaseAuth.getInstance()
        authenticator()
    }

    private fun authenticator(){
        if (firebaseAuth?.getCurrentUser() != null) {
            user = FirebaseAuth.getInstance().getCurrentUser();
            Picasso.get().load(firebaseAuth!!.getCurrentUser()!!.getPhotoUrl()).fit().centerCrop().into(img_pic_profile);
        }
    }
}