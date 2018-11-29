package edu.uw.maps101.seattlespothunter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/***********************************************************************
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 **********************************************************************/
class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
//        var fragment: Fragment
////        when (position) {
////            0 -> fragment = MapFragment.newInstance()
////            1 -> fragment = MapFragment().newInstance()
////            else -> fragment = MapFragment().newInstance()
////        }
////        return fragment
        return MapFragment.newInstance()
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence = when (position) {
        0 -> "Tab 1 Item"
        1 -> "Tab 2 Item"
        2 -> "Tab 3 Item"
        else -> ""
    }

}
