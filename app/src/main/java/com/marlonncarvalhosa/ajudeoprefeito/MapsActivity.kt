package com.marlonncarvalhosa.ajudeoprefeito

import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var firebaseAuth: FirebaseAuth? = null
    private var user = FirebaseAuth.getInstance().currentUser
    private var locationManager: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_maps);
        this.getSupportActionBar()?.hide();

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        firebaseAuth = FirebaseAuth.getInstance()
        authenticator()

    }

    private fun authenticator() {
        if (firebaseAuth?.getCurrentUser() != null) {
            user = FirebaseAuth.getInstance().getCurrentUser();
            Picasso.get().load(firebaseAuth!!.getCurrentUser()!!.getPhotoUrl()).into(btn_profile)

            btn_profile.setOnClickListener(View.OnClickListener {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            })

        } else (
                btn_profile.setOnClickListener(View.OnClickListener {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                })
                )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val zoom = 15f
        val home = LatLng(-21.1955, -41.8965)
        mMap.addMarker(MarkerOptions().position(home).title("Home"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, zoom))
    }
}