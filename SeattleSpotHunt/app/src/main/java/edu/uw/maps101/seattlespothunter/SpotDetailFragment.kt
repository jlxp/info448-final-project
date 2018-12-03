package edu.uw.maps101.seattlespothunter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_spot_detail.view.*

class SpotDetailFragment : Fragment() {

    internal interface Toolbar {
        fun setUpToolbar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_spot_detail, container, false)
        arguments?.let {
            val spot = it.getParcelable<SpotList.Spot>(SPOT_DETAIL_ID)
            view.detail_name.text = spot.name
            view.detail_desc.text = spot.desc
        }
        return view
    }

    companion object {

        const val SPOT_DETAIL_ID = "spot_details_id"

        @JvmStatic
        fun newInstance(spot: SpotList.Spot) : SpotDetailFragment {
            return SpotDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(SPOT_DETAIL_ID, spot)
                }
            }
        }
    }
}
