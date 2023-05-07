package com.umsl.wdhq58.weatherfinal

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.location.LocationRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LastLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var latLongPos: Pair<Double, Double> = Pair(0.0,0.0)
    private var locationRequest: CurrentLocationRequest = CurrentLocationRequest.Builder()
        .setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
        .setPriority(LocationRequest.QUALITY_HIGH_ACCURACY)
        .setDurationMillis(5000)
        .build()
    private var lastLocation: LastLocationRequest = LastLocationRequest.Builder()
        .setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
        .build();


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
                //get position and create map fragment
                fusedLocationProviderClient.getCurrentLocation(locationRequest, null)
                    .addOnSuccessListener { location : Location? ->
                        if (location != null) {

                            latLongPos = Pair(location.latitude, location.longitude)
                            initMap()
                            initWeather()
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


    }

    override fun onMapReady(map: GoogleMap) {
        val location   = LatLng(latLongPos.first, latLongPos.second)
        val camPosition = CameraUpdateFactory.newLatLng(location)
        val camZoom = CameraUpdateFactory.zoomTo(15F)
        map.moveCamera(camPosition)
        map.animateCamera(camZoom)
        map.addMarker(MarkerOptions()
            .position(location)
            .title("You Are Here"))




    }

    private fun initMap(){
        //map fragment
        val mapFragment = SupportMapFragment.newInstance()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.mapFragment, mapFragment)
            .commit()

        mapFragment.getMapAsync(this)
    }

    private fun initWeather(){
        val weatherFragment = weather.newInstance(latLongPos.first, latLongPos.second)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.weatherFragment, weatherFragment)
            .commit()
    }

}