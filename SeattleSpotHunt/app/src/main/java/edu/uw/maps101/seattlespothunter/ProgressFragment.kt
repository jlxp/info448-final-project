package edu.uw.maps101.seattlespothunter

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.brkckr.circularprogressbar.CircularProgressBar
import java.util.*

class ProgressFragment : Fragment() {

    private lateinit var circularProgressBar: CircularProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_progress_list, container, false)

        circularProgressBar = view.findViewById(R.id.circularProgressBar)
        val percent = SpotList.getPercent()
        circularProgressBar.progressValue = percent.toFloat()
        view.findViewById<TextView>(R.id.progress_percentage).text = percent.toString() + "%"
        view.findViewById<RecyclerView>(R.id.progress_list).adapter = MyProgressRecyclerViewAdapter(arguments!!.getParcelableArrayList(MapFragment.LIST_ID))

        return view
    }

    companion object {
        fun newInstance(currentList: List<SpotList.Spot>) = ProgressFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(MapFragment.LIST_ID, currentList as ArrayList<out Parcelable>)
            }
        }
    }
}
