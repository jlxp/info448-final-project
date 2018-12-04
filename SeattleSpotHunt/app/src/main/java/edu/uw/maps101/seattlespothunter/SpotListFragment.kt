package edu.uw.maps101.seattlespothunter

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.uw.maps101.seattlespothunter.MapFragment.Companion.LIST_ID
import java.util.*

class SpotListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_spotlist_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                adapter = MySpotListRecyclerViewAdapter(arguments!!.getParcelableArrayList(LIST_ID))
            }
        }
        return view
    }

    companion object {
        fun newInstance(currentList: List<SpotList.Spot>) = SpotListFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(LIST_ID, currentList as ArrayList<out Parcelable>)
            }
        }
    }
}
