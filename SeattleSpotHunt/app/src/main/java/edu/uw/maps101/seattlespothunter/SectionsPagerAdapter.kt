package edu.uw.maps101.seattlespothunter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment
        when (position) {
            0 -> fragment = MapFragment.newInstance()
            1 -> fragment = SpotListFragment.newInstance()
            2 -> fragment = ProgressFragment.newInstance()
            else -> fragment = MapFragment.newInstance()
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
