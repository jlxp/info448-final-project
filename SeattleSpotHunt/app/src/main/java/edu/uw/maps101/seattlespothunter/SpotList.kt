package edu.uw.maps101.seattlespothunter

import android.util.Log
import com.google.android.gms.maps.model.LatLng

class SpotList: ArrayList<Spot>() {
    val list = ArrayList<Spot>()

    init {
        addAllSpots() //initalize list
    }


    //This helper function adds 20 new spot objects to the Array list for each of the predetermined tourists spots and intializes all of them to
    private fun addAllSpots() {
        val name = get(R.string.spot1name)
        Log.v("SpotList.name", get(R.string.spot1name).name)
        Log.v("SpotList as String", get(R.string.spot1name) as String)
        Log.v("SpotList.toString()", get(R.string.spot1name).toString())

        // 1 Space Needle LatLng(47.6206537, -122.3487383)
        list.add(Spot(name, get(R.string.spot1desc) as String, false, LatLng(0.0, 0.0)))
        // 2 Pike Place Market LatLng(47.608987, -122.340682)
        // 3 Museum of Pop Culture LatLng(47.621492, -122.348123)
        // 4 Chihuly Garden and Glass LatLng(47.620553, -122.350455)
        // 5 Seattle Art Mueseum LatLng(47.607701,-122.338558)
        // 6 Kerry Park LatLng(47.629471,-122.359924)
        // 7 Woodland Park Zoo LatLng(47.669258,-122.350906)
        // 8 Seattle Aquarium LatLng(47.603230,-122.330276)
        // 9 Gas Works Park LatLng(47.645599,-122.334930)
        // 10 Gum Wall LatLng(47.608124,-122.340157)
        // 11 Pacific Science Center LatLng(47.619340,-122.350778)
        // 12 Seattle Great Wheel LatLng(47.606220, -122.342517)
        // 13 Fremont Troll LatLng(47.651048,-122.347235)
        // 14 Golden Gardens Park LatLng(47.692200,-122.402980)
        // 15 Olympic Sculpture Park LatLng(47.616238,-122.354313)
        // 16 Washington Park Arboretum | UW Botanic Gardens LatLng(47.635185,-122.296471)
        // 17 Smith Tower LatLng(47.601958,-122.331738)
        // 18 University of Washington - Seattle Campus LatLng(47.655923, -122.309345)
        // 19 Seattle Japanese Gardens LatLng(47.629839,-122.297179)
        // 20 Coleman Dock LatLng(47.601716,-122.336667)

    }

}

data class Spot (
    val name: String,
    val desc: String,
    val cost: Boolean,
    val latLng: LatLng,
    var visited: Boolean = false
)