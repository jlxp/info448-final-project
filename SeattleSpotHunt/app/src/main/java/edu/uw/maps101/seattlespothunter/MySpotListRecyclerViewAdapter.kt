package edu.uw.maps101.seattlespothunter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_spotlist.view.*

class MySpotListRecyclerViewAdapter(
    private val mValues: List<SpotList.Spot>
) : RecyclerView.Adapter<MySpotListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_spotlist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mName.text = item.name
        holder.mDesc.text = item.desc

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
        val mName = mView.name
        val mDesc = mView.desc
    }
}
