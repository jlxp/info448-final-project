package edu.uw.maps101.seattlespothunter

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : SupportMapFragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity as Activity)
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
            mMap.getUiSettings().setMyLocationButtonEnabled(true)
            setSpotsOnMap()

        } else {
            ActivityCompat.requestPermissions(activity as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 2)
        }

    }

    //This methods adds all markers for the current version of SpotList
    fun setSpotsOnMap() {
        for (spot in SpotList.list) {
            val mOptions = MarkerOptions().position(spot.latLng).title(spot.name)

            if (spot.cost) {
                mOptions.snippet("$")
            }

            if (spot.visited) {
                mOptions.icon(BitmapDescriptorFactory.defaultMarker(0.toFloat()))
            } else {
                mOptions.icon(BitmapDescriptorFactory.defaultMarker(124.toFloat()))
            }
            mMap.addMarker(mOptions)
        }
    }

    companion object {

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(): MapFragment = MapFragment()
    }
}