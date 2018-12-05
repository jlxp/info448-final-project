package edu.uw.maps101.seattlespothunter

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.brkckr.circularprogressbar.CircularProgressBar
import com.google.android.gms.maps.model.LatLng
import java.util.*

class ProgressFragment : Fragment() {

    private lateinit var circularProgressBar: CircularProgressBar
    private lateinit var currentList: ArrayList<SpotList.Spot>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        currentList = arguments!!.getParcelableArrayList(MapFragment.LIST_ID)
        val view = inflater.inflate(R.layout.fragment_progress_list, container, false)

        circularProgressBar = view.findViewById(R.id.circularProgressBar)
        val percent = getPercent().toInt()
        circularProgressBar.progressValue = percent.toFloat()
        view.findViewById<TextView>(R.id.progress_percentage).text = percent.toString() + "%"
        view.findViewById<RecyclerView>(R.id.progress_list).adapter = MyProgressRecyclerViewAdapter(
            arguments!!.getParcelableArrayList(MapFragment.LIST_ID),
            arguments!!.getDouble("lat"),
            arguments!!.getDouble("lng"))

        return view
    }

    private fun getPercent(): Double {
        var count = 0
        for (spot in currentList) {
            if (spot.visited) {
                count++
            }
        }
        return 100.0 * count / currentList.size
    }

    companion object {
        fun newInstance(currentList: List<SpotList.Spot>, currentLocation: LatLng) = ProgressFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(MapFragment.LIST_ID, currentList as ArrayList<out Parcelable>)
                putDouble("lat", currentLocation.latitude)
                putDouble("lng", currentLocation.longitude)
            }
        }
    }
}
