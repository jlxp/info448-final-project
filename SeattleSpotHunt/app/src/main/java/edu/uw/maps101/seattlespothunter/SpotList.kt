package edu.uw.maps101.seattlespothunter

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


object SpotList {
    val LIST: MutableList<Spot> = ArrayList()

    init {
        addAllSpots()
    }


    //This helper function adds 20 new spot objects to the Array list for each of the predetermined tourists spots and intializes all of them to
    private fun addAllSpots() {
        LIST.add(Spot("Space Needle","A classic landmark of Seattle", false, 0, 0))
        //Pit Stop Total: 20
        //Space Needle
        //Pike Place
        //Pacific Science Center
        //MoPop
        //Living Computer Musuem
        //UW
        //Seattle Art Musesum
        //Chehuly
    }

    fun getPercent(): Double {
        var count = 0
        for (spot in LIST) {
            if (spot.visited) {
                count++
            }
        }
        return 0.0 + count / LIST.size
    }

    /**
     * TODO: just a thought but shouldn't we include the actual address and and image of the place too?
     */
    @Parcelize
    data class Spot (
        val name: String,
        val desc: String,
        val cost: Boolean,
        val lat: Int,
        val long: Int,
        var visited: Boolean = false
    ) : Parcelable
}
