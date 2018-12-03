package edu.uw.maps101.seattlespothunter

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_spot_detail.*

class SpotDetailActivity : AppCompatActivity(), SpotDetailFragment.Toolbar {

    // this doesn't do anything but theoretically it should set the toolbar title
    // I had to set it manually from the xml because this doesn't work
    override fun setUpToolbar() {
        detail_toolbar?.title = " "
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spot_detail)
        setSupportActionBar(detail_toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            val fragment = SpotDetailFragment.newInstance(intent.getParcelableExtra(SpotDetailFragment.SPOT_DETAIL_ID))
            supportFragmentManager.beginTransaction()
                .add(R.id.spot_detail_container, fragment)
                .commit()
        }

//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                navigateUpTo(Intent(this, fragmentManager.popBackStack().javaClass))
                true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
