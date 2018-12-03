package edu.uw.maps101.seattlespothunter

import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

import android.Manifest
import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Color.parseColor
import android.location.Location
import android.support.v7.app.AppCompatActivity
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
import com.google.android.gms.maps.model.*
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

class MapFragment : SupportMapFragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val TAG = "MapFragment"

    private var mContext: Context? = null

    private val LAST_LOCATION_REQUEST_CODE = 1
    private val ONGOING_LOCATION_REQUEST_CODE = 2

    private val noLocation = -500.000
    private var lat = noLocation
    private var lng = noLocation

    private var lastPitStopInRange = ""

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getMapAsync(this)

        var builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity as Activity)
        getLastLocation() //first time


    }

    // If the user walks in radius of one of the pit stops that they have not yet visited,
    // send them a notification
    private fun updatePitStopsIfWeWalkWithinRadiusOfAPitStop(myLoc: Location?) {
        //Distance in meters

        // _______________
        // A. Closeby notification - within 300 meters, about 1/5 a mile
        // 1. Doesn't occur if already reached
        // 2. Mark the last visited closeby pitstop in this fragment
        // 3. Don't send this notification if last visited = the same one
        // 4. Don't send if already visited

        // ________________
        // B. Reached notification - within 100 meters (328 feet)
        // 1. Save in local storage
        // 2. Update the progress bar
        // 3. Make the target point turn green
        // 4. Send notification
        // 5. Don't send if already visited

        // Internal storage:
        //      Key (name of spot)
        //      value: Visited/not visited. true/false

        if (myLoc != null) {

            /*
                data class Spot (
                val name: String,
                val desc: String,
                val cost: Boolean,
                val lat: Int,
                val long: Int,
                var visited: Boolean = false
            )
            */
            // Go through each of the pitstops
            // https://stackoverflow.com/questions/2741403/get-the-distance-between-two-geo-points

            // For testing... set this to whatever you want
            // myLoc.latitude = 0.00
            // myLoc.longitude = 0.00

            SpotList.LIST.forEach() {
                if (it.visited == false) {
                    val pitstopLoc = Location("")
                    pitstopLoc.latitude = it.lat.toDouble()
                    pitstopLoc.longitude = it.long.toDouble()

                    val distanceInMeters = myLoc.distanceTo(pitstopLoc)

                    // If close distance
                    if (distanceInMeters <= 50) {
                        it.visited = true

                        // Send notification: You've reached the location!
                        notifyReached(it)

                        // Change the pointer to become green

                        // Update the progress bar (if necessary)

                    } else if (distanceInMeters <= 300) {
                        if (lastPitStopInRange != it.name) {
                            lastPitStopInRange = it.name
                            // Send notification: you're in range!
                            notifyAlmostReached(it)
                        }
                    }
                }
            }
        }
    }

    // Update markers to be either red or green based on visited status
    private fun updateMarkerColors() {
        mMap.clear()
        SpotList.LIST.forEach() {
            val loc = LatLng(it.lat.toDouble(), it.long.toDouble())
            val mo = MarkerOptions()
                    .position(loc)
                    .title(it.name)

            // Visited => Green
            if (it.visited == true) {
                mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            } else { // Unvisited => Red
                mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            }
            // When you click on any marker, go to the descriptive view of it
            mMap.addMarker(mo)
            goToDescriptiveView(it.name)
        }
    }

    // See
    private fun goToDescriptiveView(pitStopName: String) {
        // Some intent stuff goes here...
    }

    // Within 50 meters of pit stop
    private fun notifyReached(spot: SpotList.Spot) {
        Toast.makeText(mContext, "You made it to the ${spot.name}!", Toast.LENGTH_LONG).show()
        Log.v(TAG, "888AAA")

        // Check if notifications are enabled in settings

        // https://github.com/info448-au18/yama-greycabb/blob/master/app/src/main/java/edu/uw/greycabb/yama/MySmsReceiver.kt
    }

    // Within 300 meters of pit stop
    private fun notifyAlmostReached(spot: SpotList.Spot) {
        Toast.makeText(mContext, "You're almost at the ${spot.name}!", Toast.LENGTH_LONG).show()
        Log.v(TAG, "999BBB")
    }

    // When location is updated
    private fun displayLocation(location: Location?) {
        Log.v(TAG, "Received location: $location")
        Toast.makeText(mContext, "Got location: $location", Toast.LENGTH_SHORT).show()

        if (location != null) {
            updatePitStopsIfWeWalkWithinRadiusOfAPitStop(location)
            Toast.makeText(mContext, "Got location", Toast.LENGTH_SHORT).show()

            val newLL = LatLng(location.latitude, location.longitude)

            // Move map to start position on start
            if (lat == noLocation) {
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
        mContext = activity
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
                ActivityCompat.requestPermissions(activity as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LAST_LOCATION_REQUEST_CODE)
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
                ActivityCompat.requestPermissions(activity as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), ONGOING_LOCATION_REQUEST_CODE)
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
        //val sydney = LatLng(-34.0, 151.0)
        //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        // Load in the markers, with their respective colors
        // Red marker = not visited yet
        // Green marker = visited
        updateMarkerColors()
    }

    companion object {

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(): MapFragment = MapFragment()
    }
}