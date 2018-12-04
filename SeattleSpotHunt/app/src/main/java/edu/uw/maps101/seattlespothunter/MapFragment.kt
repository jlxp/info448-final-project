package edu.uw.maps101.seattlespothunter

import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import android.support.v7.app.AppCompatActivity
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Color.parseColor
import android.location.Location
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import com.google.android.gms.location.*
import android.net.Uri
import android.os.StrictMode
import android.support.v4.app.ActivityCompat
import com.google.android.gms.location.LocationRequest
import android.support.v4.content.ContextCompat
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.ShareActionProvider
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader


class MapFragment : SupportMapFragment(), OnMapReadyCallback {

    private var mContext: Context? = null

    private lateinit var mMap: GoogleMap
    private val TAG = "Map"

    private val LAST_LOCATION_REQUEST_CODE = 1
    private val ONGOING_LOCATION_REQUEST_CODE = 2

    private val noLocation = -500.000
    private var lat = noLocation
    private var lng = noLocation

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getMapAsync(this)

        var builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build());

        val mc = mContext; // This has to be done since mContext is nullable
        if (mc != null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(mc)
        }
        //first fun goes here
        getLastLocation()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    companion object {

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(): MapFragment = MapFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }





    // When location is updated
    private fun displayLocation(location: Location?) {
        if (location != null) {
            Toast.makeText(activity, "Received location: $location", Toast.LENGTH_SHORT).show()
            val newLL = LatLng(location.latitude, location.longitude)

            // Move map to start position on start
            if (lat == noLocation) {
                val marker = MarkerOptions().position(newLL).title("Starting location")
                mMap.addMarker(marker)
                mMap.moveCamera(CameraUpdateFactory.newLatLng(newLL))
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLng(newLL))

            // Don't draw new polyline if we haven't moved at all
            if (location.latitude != lat || location.longitude != lng) {
                lat = location.latitude
                lng = location.longitude
            }
        }
    }

    override fun onStart() {
        super.onStart()
        startLocationUpdates()
    }
    override fun onStop() {
        super.onStop()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    fun getLastLocation() {
        val mc = mContext
        if (mc != null) {
            val permissionCheck = ContextCompat.checkSelfPermission(mc, Manifest.permission.ACCESS_FINE_LOCATION)
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                //access last location, asynchronously!
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    //Log.v(TAG, "$location")

                    displayLocation(location)
                }
            } else {
                ActivityCompat.requestPermissions(
                    this.activity as Activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LAST_LOCATION_REQUEST_CODE
                )
            }
        }
    }

    fun startLocationUpdates() {
        val mc = mContext
        if (mc != null) {
            val permissionCheck = ContextCompat.checkSelfPermission(mc, Manifest.permission.ACCESS_FINE_LOCATION)
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                val locationRequest = LocationRequest().apply {
                    interval = 10000
                    fastestInterval = 5000
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }
                locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult?) {
                        locationResult ?: return
                        displayLocation(locationResult.locations[0])
                    }
                }
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
            } else {
                ActivityCompat.requestPermissions(
                    this.activity as Activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    ONGOING_LOCATION_REQUEST_CODE
                )
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LAST_LOCATION_REQUEST_CODE -> { //if asked for last location
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation() //do whatever we'd do when first connecting (try again)
                }
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
            ONGOING_LOCATION_REQUEST_CODE -> { //if asked for last location
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates() //do whatever we'd do when first connecting (try again)
                }
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }











}