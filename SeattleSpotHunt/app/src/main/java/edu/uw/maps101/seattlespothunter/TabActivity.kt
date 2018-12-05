package edu.uw.maps101.seattlespothunter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.model.LatLng
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_tab.*
import java.io.File

class TabActivity : AppCompatActivity(), MapFragment.OnSpotVisitedListener {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var currentList: ArrayList<SpotList.Spot> = SpotList.list
    private var currentLocation: LatLng = LatLng(0.0, 0.0)
    // Get the directory for the app's private documents directory.
    private lateinit var file: File
    private lateinit var filePath: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab)

        setSupportActionBar(toolbar)
        file = File(this@TabActivity.getExternalFilesDir(
            Environment.DIRECTORY_DOCUMENTS), "CacheData") // ~/Documents/CacheData

        filePath = Uri.Builder().appendPath(file.path).appendPath("cache.json").build()

        var existingFile = File(filePath.path)
        if (existingFile.exists()) {
            // read in the data and set currentList to that list
            val gson = GsonBuilder().setPrettyPrinting().create()

            val bufferedReader = existingFile.bufferedReader()
            val jsonSpotList = bufferedReader.use { it.readText() }

            val type = object : TypeToken<ArrayList<SpotList.Spot>>() {}.type
            val resultList = gson.fromJson<ArrayList<SpotList.Spot>>(jsonSpotList, type)
            currentList = resultList
        } else {
            currentList = SpotList.list
        }
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager, currentList, currentLocation)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        tab_layout.setupWithViewPager(container)
        tab_layout.setSelectedTabIndicatorColor(resources.getColor(R.color.white))
    }

    override fun updateCurrentList(currentList: List<SpotList.Spot>) {
        this.currentList = currentList as ArrayList<SpotList.Spot>
        mSectionsPagerAdapter!!.currentList = currentList
        saveFile()
    }

    override fun passCurrentLocation(latLng: LatLng) {
        currentLocation = latLng
        mSectionsPagerAdapter!!.currentLocation = currentLocation
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_tab, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun saveFile() {
        if (!file.mkdirs()) {
            Log.e("TabActivity", "Directory not created")
            // will throw this message if the directory already exists due to rebuilding the app many times on the phone
        }

        var intentService = Intent(this@TabActivity, SaveIntentService::class.java)
        intentService.putExtra("filePath", filePath.path)
        intentService.putExtra("spotList", currentList)
        startService(intentService)
    }
}
