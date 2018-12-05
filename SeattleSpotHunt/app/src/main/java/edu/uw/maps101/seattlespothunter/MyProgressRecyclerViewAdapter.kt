package edu.uw.maps101.seattlespothunter

import android.content.Intent
import android.location.Location
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import kotlinx.android.synthetic.main.fragment_progress.view.*
import java.util.*

class MyProgressRecyclerViewAdapter(
    private val mValues: List<SpotList.Spot>,
    val lat: Double,
    val lng: Double
) : RecyclerView.Adapter<MyProgressRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_progress, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        if (item.visited) {
            holder.mIcon.setImageResource(R.drawable.ic_checkmark)
        } else {
            holder.mIcon.setImageResource(R.drawable.ic_clear_checkmark)
        }
        holder.mName.text = item.name

        val itemLocation = Location("")
        itemLocation.latitude = item.latLng.latitude
        itemLocation.longitude = item.latLng.longitude

        val currentLocation = Location("")
        currentLocation.latitude = lat
        currentLocation.longitude = lng

        val distance = (currentLocation.distanceTo(itemLocation) * 0.000621371192).toInt()

        var endString: String
        if (distance == 1) {
            endString = " mile away"
        } else {
            endString = " miles away"
        }

        holder.nDistance.text = "" + distance + endString

        with(holder.mView) {
            tag = item
            setOnClickListener(showDetails())
        }
    }

    private fun showDetails(): View.OnClickListener {
        return View.OnClickListener { v ->
            val item = v.tag as SpotList.Spot
            val intent = Intent(v.context, SpotDetailActivity::class.java).apply {
                putExtra(SpotDetailFragment.SPOT_DETAIL_ID, item)
            }
            v.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIcon: ImageView = mView.progress_icon
        val mName: TextView = mView.progress_list_name
        val nDistance: TextView = mView.progress_list_distance
    }
}
