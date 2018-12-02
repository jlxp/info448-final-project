package edu.uw.maps101.seattlespothunter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brkckr.circularprogressbar.CircularProgressBar

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ProgressFragment.OnListFragmentInteractionListener] interface.
 */
class ProgressFragment : Fragment() {

    // TODO: Customize parameters
//    private var columnCount = 1

    private lateinit var circularProgressBar: CircularProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
//            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_progress_list, container, false)

        /**
         * the progress bar gets updated in the map, I think, since whenever the user is at the spot the data will be
         * changed to visited and then the circle progress should also be updated...???
         */
        circularProgressBar = view.findViewById(R.id.circularProgressBar)

//        circularProgressBar.progressValue

        view.findViewById<RecyclerView>(R.id.progress_list).adapter = MyProgressRecyclerViewAdapter(SpotList.LIST)

        return view
    }

    companion object {
//        // TODO: Customize parameter argument names
//        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        fun newInstance() = ProgressFragment()
    }
}
