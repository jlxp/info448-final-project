package edu.uw.maps101.seattlespothunter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import kotlinx.android.synthetic.main.fragment_progress.view.*

class MyProgressRecyclerViewAdapter(
    private val mValues: List<SpotList.Spot>
) : RecyclerView.Adapter<MyProgressRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_progress, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        // TODO put in if statement to determine what icon to use here
        holder.mIcon.setImageResource(R.drawable.ic_clear_checkmark)
        holder.mName.text = item.name
        holder.nDistance.text = "" + item.lat + " " + item.long

        with(holder.mView) {
            tag = item
            setOnClickListener(showDetails())
        }
    }

    private fun showDetails(): View.OnClickListener {
        return View.OnClickListener { v ->
            val item = v.tag as SpotList.Spot
            val intent = Intent(v.context, SpotDetailActivity::class.java)
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
