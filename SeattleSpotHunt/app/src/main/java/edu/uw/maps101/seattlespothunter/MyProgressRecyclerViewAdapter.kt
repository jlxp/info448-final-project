package edu.uw.maps101.seattlespothunter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView


import edu.uw.maps101.seattlespothunter.ProgressFragment.OnListFragmentInteractionListener
import edu.uw.maps101.seattlespothunter.dummy.DummyContent.DummyItem

import kotlinx.android.synthetic.main.fragment_progress.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyProgressRecyclerViewAdapter(
    private val mValues: List<SpotList.Spot>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyProgressRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as SpotList.Spot
            val intent = Intent(v.context, SpotDetailActivity::class.java)
            v.context.startActivity(intent)

            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

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
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIcon: ImageView = mView.progress_icon
        val mName: TextView = mView.progress_list_name
        val nDistance: TextView = mView.progress_list_distance
    }
}
