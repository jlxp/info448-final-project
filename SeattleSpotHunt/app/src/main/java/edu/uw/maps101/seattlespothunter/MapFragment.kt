package edu.uw.maps101.seattlespothunter

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : SupportMapFragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
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
        mMap.moveCamera(CameraUpdateFactory.newLatLng(seattle))

        //Initialize Google Play Services
        val permissionCheck = ContextCompat.checkSelfPermission(, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //access last location, asynchronously!
            mMap.setMyLocationEnabled(true);
//            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location ->
//                currentLatLng = LatLng(location.latitude, location.longitude)
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 20.toFloat()))
//                saveFile()
//            }
        } else {
            //ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LAST_LOCATION_REQUEST_CODE)
        }

        mMap.getUiSettings().setMyLocationButtonEnabled(true)
    }

    fun setSpotsOnMap() {

    }

    companion object {

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(): MapFragment = MapFragment()
    }
}