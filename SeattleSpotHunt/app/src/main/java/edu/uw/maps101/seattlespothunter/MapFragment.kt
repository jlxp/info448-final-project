package edu.uw.maps101.seattlespothunter

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.os.StrictMode
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*
import kotlin.math.ln

class MapFragment : SupportMapFragment(), OnMapReadyCallback {

    private var mContext: Context? = null

    private lateinit var mMap: GoogleMap
    private val TAG = "MapFragment"

    private val LAST_LOCATION_REQUEST_CODE = 1
    private val ONGOING_LOCATION_REQUEST_CODE = 2

    private val noLocation = -500.000
    private var lat = noLocation
    private var lng = noLocation

    private var lastPitStopInRange = ""

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private var notificationManager: NotificationManager? = null
    private var CHANNEL_NAME : String = "notification"

    private lateinit var currentList: ArrayList<SpotList.Spot>

    private var mOnSpotVisitedListener: OnSpotVisitedListener? = null

    internal interface OnSpotVisitedListener {
        fun updateCurrentList(currentList: List<SpotList.Spot>)
        fun passCurrentLocation(latLng: LatLng)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getMapAsync(this)

        var builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build());

        val mc = mContext; // This has to be done since mContext is nullable
        if (mc != null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(mc)
        }
        getLastLocation()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity as Activity)
        getLastLocation() //first time

        currentList = arguments!!.getParcelableArrayList(LIST_ID)

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context
        mOnSpotVisitedListener = context as? OnSpotVisitedListener
        if (mOnSpotVisitedListener == null) {
            throw ClassCastException("$context must implement OnSpotVisitedListener")
        }
    }

    // If the user walks in radius of one of the pit stops that they have not yet visited,
    // send them a notification
    private fun updatePitStopsIfWeWalkWithinRadiusOfAPitStop(myLoc: Location?) {

        if (myLoc != null) {

            // Location testing
            myLoc.latitude = 47.692200
            myLoc.longitude = -122.402980

            // Go through each of the pitstops
            // https://stackoverflow.com/questions/2741403/get-the-distance-between-two-geo-points

            var hitOne = false

            currentList.forEach() {
                if (!hitOne && !it.visited) {
                    val pitStopLoc = Location("")
                    pitStopLoc.latitude = it.latLng.latitude
                    pitStopLoc.longitude = it.latLng.longitude

                    val distanceInMeters = myLoc.distanceTo(pitStopLoc)

                    // If close distance
                    if (distanceInMeters <= 50) {
                        it.visited = true
                        mOnSpotVisitedListener!!.updateCurrentList(currentList)
                        // Send notification: You've reached the location!
                        notifyReached(it)
                        setSpotsOnMap()

                        hitOne = true

                        // Change the pointer to become green

                        // Update the progress bar (if necessary)

                    } else if (distanceInMeters <= 100) {
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

    // See
    private fun goToDescriptiveView(pitStopName: String) {
        // Some intent stuff goes here...
    }

    // Within 50 meters of pit stop
    private fun notifyReached(spot: SpotList.Spot) {

        val preferences = PreferenceManager.getDefaultSharedPreferences(mContext)
        val notificationsEnabled = preferences.getBoolean("notifications_new_message", true)

        val msg = "You made it to the ${spot.name}!"
        Toast.makeText(mContext, "$msg", Toast.LENGTH_LONG).show()

        if (notificationsEnabled) {

            // https://github.com/info448-au18/yama-greycabb/blob/master/app/src/main/java/edu/uw/greycabb/yama/MySmsReceiver.kt
            sendNotification(spot, "$msg", "Congratulations!", "${spot.desc}")
        }
    }

    // Within 300 meters of pit stop
    private fun notifyAlmostReached(spot: SpotList.Spot) {

        val preferences = PreferenceManager.getDefaultSharedPreferences(mContext)
        val notificationsEnabled = preferences.getBoolean("notifications_new_message", true)

        var msg = "You're almost at the ${spot.name}!"
        Toast.makeText(mContext, "$msg", Toast.LENGTH_LONG).show()

        if (notificationsEnabled) {
            sendNotification(spot, "You're close to a pit stop!", "$msg", "")
        }
    }

    private fun sendNotification(spot: SpotList.Spot, title: String, msg: String, longMsg: String) {
        val mc = mContext as Context
        if (mc != null) {
            createNotificationChannel(mc)
            notificationManager =
                    mc.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channelID = CHANNEL_NAME

            // Create the notification
            val mBuilder = NotificationCompat.Builder(mc, channelID)
                .setSmallIcon(R.drawable.ic_mtrl_chip_checked_circle)
                .setContentTitle("$title")
                .setContentText("$msg")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(NotificationCompat.BigTextStyle()
                    .bigText("$msg $longMsg"))
            ///.setContentIntent(intent)
            //.addAction(R.drawable.design_password_eye, "View", intent)

            // Send out the notification
            with(NotificationManagerCompat.from(mc)) {
                notify(109, mBuilder.build())
            }
        }
    }

    // Create notification channel on API > 26
    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_NAME
            val descriptionText = "Spot Notification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(name, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
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

        //move the camera to Seattle
        val seattle = LatLng(47.608013, -122.335167)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seattle, 10.toFloat()))

        val permissionCheck = ContextCompat.checkSelfPermission(context as Context, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true
            setSpotsOnMap()

        } else {
            ActivityCompat.requestPermissions(activity as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 2)
        }

    }

    //This methods adds all markers for the current version of SpotList
    private fun setSpotsOnMap() {
        mMap.clear()

        for (spot in currentList) {
            val mOptions = MarkerOptions().position(spot.latLng).title(spot.name)

            if (spot.cost) {
                mOptions.snippet("$")
            }

            if (spot.visited) {
                mOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            } else {
                mOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            }
            mMap.addMarker(mOptions)

            // When you click on any marker, go to the descriptive view of it
        }
    }

    companion object {

        const val LIST_ID = "spot_list"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(currentList: List<SpotList.Spot>): MapFragment = MapFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(LIST_ID, currentList as ArrayList<out Parcelable>)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }

    // When location is updated
    private fun updateLocation(location: Location?) {

        //Log.v(TAG, "Received location: $location")
        if (location != null) {
            updatePitStopsIfWeWalkWithinRadiusOfAPitStop(location)
            //Toast.makeText(activity, "Received location: $location", Toast.LENGTH_SHORT).show()
            val newLL = LatLng(location.latitude, location.longitude)

            // Move map to start position on start
            if (lat == noLocation) {
                //val marker = MarkerOptions().position(newLL).title("Starting location")
                //mMap.addMarker(marker)
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(newLL))
            }

            if (location.latitude != lat || location.longitude != lng) {
                lat = location.latitude
                lng = location.longitude
            }
            mOnSpotVisitedListener!!.passCurrentLocation(LatLng(lat, lng))
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

    private fun getLastLocation() {
        val mc = mContext
        if (mc != null) {
            val permissionCheck = ContextCompat.checkSelfPermission(mc, Manifest.permission.ACCESS_FINE_LOCATION)
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                //access last location, asynchronously!
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    //Log.v(TAG, "$location")

                    updateLocation(location)
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

    private fun startLocationUpdates() {
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
                        updateLocation(locationResult.locations[0])
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