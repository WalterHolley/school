package com.umsl.wdhq58.weatherfinal

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var latLongPos: Pair<Double, Double> = Pair(0.0,0.0)



    //check permissions
    private val locationPermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    )   {
        permissions -> when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                //access granted
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                //approximate access granted

            } else -> {
                //No access granted
                Toast.makeText(this.applicationContext, "Cannot access location:  Permissions required", Toast.LENGTH_SHORT)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        //location client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        //location permissions
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), ContextCompat.checkSelfPermission(this.applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {
                //perform action
                fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener { location : Location? ->
                        if (location != null) {
                            latLongPos = Pair(location.latitude, location.longitude)
                        }
                    }
            }
            else -> {
                locationPermissions.launch(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ))
            }
        }

        //map fragment
        val mapFragment = SupportMapFragment.newInstance()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.mapFragment, mapFragment)
            .commit()

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        map.addMarker(MarkerOptions()
            .position(LatLng(latLongPos.first, latLongPos.second))
            .title("You Are Here"))
    }

}