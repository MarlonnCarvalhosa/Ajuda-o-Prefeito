package com.marlonncarvalhosa.ajudeoprefeito

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    lateinit var mapView: MapView
    var firebaseAuth: FirebaseAuth? = null
    private var user = FirebaseAuth.getInstance().currentUser
    private var locationManager: LocationManager? = null
    private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"
    private val DEFAULT_ZOOM = 15f
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps);

        mapView = findViewById(R.id.map)

        askGalleryPermissionLocation()

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar()?.hide();

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY)
        }

        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        firebaseAuth = FirebaseAuth.getInstance()
        authenticator()

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mapView.onResume()
        mMap = googleMap

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        mMap!!.isMyLocationEnabled()
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

    private fun askGalleryPermissionLocation() {
        askPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) {
            getCurrentLocation()
        }.onDeclined { e ->
            if (e.hasDenied()) {

                AlertDialog.Builder(this)
                        .setMessage("Aceota a permissao")
                        .setPositiveButton("sim") { _, _ -> e.askAgain() }
                        .setNegativeButton("não") { dialog, which ->
                            dialog.dismiss()
                        }
                        .show()
            }
            if (e.hasForeverDenied()) {
                e.goToSettings()
            }

        }

    }

    private fun getCurrentLocation() {
        TODO("Not yet implemented")
    }


    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        askGalleryPermissionLocation()
        var mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle)
        }

        mapView.onSaveInstanceState(mapViewBundle)
    }
}