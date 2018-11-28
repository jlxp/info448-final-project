package edu.uw.maps101.seattlespothunter

import com.google.android.gms.maps.model.LatLng

class SpotList: ArrayList<Spot>() {
    val list = ArrayList<Spot>()

    init {
        addAllSpots()
    }


    //This helper function adds 20 new spot objects to the Array list for each of the predetermined tourists spots and intializes all of them to
    private fun addAllSpots() {
        list.add(Spot(R.strings.spot1name, R.strings.spot1desc, false, LatLng(R.locations.spot1lat, R.location))) //Space Needle
        //Space Needle
        //Pike Place
        //Pacific Science Center
        //MoPop
        //Living Computer Musuem
        //UW
        //Seattle Art Musesum
        //Chehuly
    }

}

data class Spot (
    val name: String,
    val desc: String,
    val cost: Boolean,
    val latLng: LatLng,
    var visited: Boolean = false
)