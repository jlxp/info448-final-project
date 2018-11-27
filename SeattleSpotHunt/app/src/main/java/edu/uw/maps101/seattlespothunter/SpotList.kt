package edu.uw.maps101.seattlespothunter

class SpotList: ArrayList<Spot>() {
    val list = ArrayList<Spot>()

    init {
        addAllSpots()
    }


    //This helper function adds 20 new spot objects to the Array list for each of the predetermined tourists spots and intializes all of them to
    private fun addAllSpots() {
        list.add(Spot("Space Needle","A classic landmark of Seattle", false, 0, 0))
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

}

data class Spot (
    val name: String,
    val desc: String,
    val cost: Boolean,
    val lat: Int,
    val long: Int,
    var visited: Boolean = false
)