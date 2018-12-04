package edu.uw.maps101.seattlespothunter

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

object SpotList {
    val list = ArrayList<Spot>()

    init {
        addAllSpots() //initalize list
    }

    //This helper function adds 20 new spot objects to the Array list for each of the predetermined tourists spots and intializes all of them to
    private fun addAllSpots() {
        // 1 Space Needle
        list.add(Spot("Space Needle",
            "Iconic, 605-ft.-tall spire at the Seattle Center, with an observation deck and a rotating restaurant.",
            true,
            LatLng(47.6206537, -122.3487383)))
        // 2 Pike Place Market
        list.add(Spot("Pike Place Market",
            "A public market overlooking the Elliott Bay waterfront. The Market opened August 17, 1907, and is one of the oldest continuously operated public farmers' markets in the United States.",
            false,
            LatLng(47.608987, -122.340682)))
        // 3 Museum of Pop Culture
        list.add(Spot("Museum of Pop Culture",
            "Inspired by Jimi Hendrix, the museum includes items from icons ranging from Bo Diddley to Bob Dylan.",
            true,
            LatLng(47.621492, -122.348123)))
        // 4 Chihuly Garden and Glass
        list.add(Spot("Chihuly Garden and Glass",
            "Art museum and sculpture garden showcasing Dale Chihuly's large, colorful glass works.",
            true,
            LatLng(47.620553, -122.350455)))
        // 5 Seattle Art Museum
        list.add(Spot("Seattle Art Museum",
            "This roomy space hosts special exhibits and a permanent collection of both current and classic artworks.",
            true,
            LatLng(47.607701,-122.338558)))
        // 6 Kerry Park
        list.add(Spot("Kerry Park",
            "Panoramic views of Downtown and the Space Needle draw visitors to this small neighborhood park.",
            false,
            LatLng(47.629471,-122.359924)))
        // 7 Woodland Park Zoo
        list.add(Spot("Woodland Park Zoo",
            "92-acre zoo with nearly 300 species, a vintage carousel, summer concerts and an indoor play space.",
            true,
            LatLng(47.669258,-122.350906)))
        // 8 Seattle Aquarium
        list.add(Spot("Seattle Aquarium",
            "Popular spot for families offers an underwater viewing dome, coral-reef tank, otters and a touch-tank.",
            true,
            LatLng(47.603230,-122.330276)))
        // 9 Gas Works Park
        list.add(Spot("Gas Works Park",
            "Former site of an oil plant is now a 20-acre park with picnic facilities and dramatic city views.",
            false,
            LatLng(47.645599,-122.334930)))
        // 10 Gum Wall
        list.add(Spot("Gum Wall",
            "Iconic brick wall covered with chewed-up gum in an alley under Pike Place Market.",
            false,
            LatLng(47.608124,-122.340157)))
        // 11 Pacific Science Center
        list.add(Spot("Pacific Science Center",
            "Rotating science exhibits plus laser light shows, a tropical butterfly house and IMAX movie theaters.",
            true,
            LatLng(47.619340,-122.350778)))
        // 12 Seattle Great Wheel
        list.add(Spot("Seattle Great Wheel",
            "Giant Ferris wheel offering climate-controlled gondolas and a bird's-eye view of the city's landmarks.",
            true,
            LatLng(47.606220, -122.342517)))
        // 13 Fremont Troll
        list.add(Spot("Fremont Troll",
            "Troll Under the Bridge is an interactive, 6.5-ton, mixed media statue made in 1990 by local artists.",
            false,
            LatLng(47.651048,-122.347235)))
        // 14 Golden Gardens Park
        list.add(Spot("Golden Gardens Park",
            "City park draws outdoor lovers for Puget Sound and mountain views, trails and a dog off-leash area.",
            false,
            LatLng(47.692200,-122.402980)))
        // 15 Olympic Sculpture Park
        list.add(Spot("Olympic Sculpture Park",
            "An offshoot of the Seattle Art Museum, this park features numerous sculptures spread over 9 acres.",
            false,
            LatLng(47.616238,-122.354313)))
        // 16 Washington Park Arboretum | UW Botanic Gardens
        list.add(Spot("Washington Park Arboretum | UW Botanic Gardens",
            "Jointly managed by the University of Washington Botanic Gardens and the City of Seattle, this 230 acre park contains a dynamic assortment of plants, some found nowhere else in the Northwest.",
            false,
            LatLng(47.635185,-122.296471)))
        // 17 Smith Tower
        list.add(Spot("Smith Tower",
            "Iconic 1914 office tower featuring a speakeasy-style cocktail bar and a 35th-floor observatory.",
            true,
            LatLng(47.601958,-122.331738)))
        // 18 University of Washington - Seattle Campus
        list.add(Spot("University of Washington - Seattle Campus",
            "Public University with many interesting locations such as Drumheller Fountain, Suzzalo Library Reading Room, and the UW Cherry Blossom Trees.",
            false,
            LatLng(47.655923, -122.309345)))
        // 19 Seattle Japanese Gardens
        list.add(Spot("Seattle Japanese Gardens",
            "3.5-acre formal garden with a cherry orchard, water features and teahouse hosting seasonal ceremonies.",
            true,
            LatLng(47.629839,-122.297179)))
        // 20 Coleman Dock
        list.add(Spot("Coleman Dock",
            "While officially known as Pier 52, this dock serves as the Seattle terminal for all Washington State Ferries to Bainbridge Island and Bremerton.",
            true,
            LatLng(47.601716,-122.336667)))
    }

    @Parcelize
    data class Spot (
        val name: String,
        val desc: String,
        val cost: Boolean,
        val latLng: LatLng,
        var visited: Boolean = false
    ) : Parcelable
}