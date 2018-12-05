package edu.uw.maps101.seattlespothunter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import com.google.android.gms.maps.model.LatLng

class SectionsPagerAdapter(fm: FragmentManager, var currentList: List<SpotList.Spot>, var currentLocation: LatLng) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment
        when (position) {
            0 -> fragment = MapFragment.newInstance(currentList)
            1 -> fragment = SpotListFragment.newInstance(currentList)
            2 -> fragment = ProgressFragment.newInstance(currentList, currentLocation)
            else -> fragment = MapFragment.newInstance(currentList)
        }
        return fragment
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence = when (position) {
        // these are named directly in the code instead of the string file because I can't seem to get the context
        // of the fragments since they aren't initialized here
        0 -> "Map"
        1 -> "List"
        2 -> "Progress"
        else -> ""
    }
}
