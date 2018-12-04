package edu.uw.maps101.seattlespothunter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.JsonReader
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_tab.*
import java.io.File
import java.io.BufferedReader

class TabActivity : AppCompatActivity(), MapFragment.OnSpotVisitedListener {
    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    private var currentList = SpotList.list
    // Get the directory for the app's private documents directory.
    lateinit var file: File

    private lateinit var filePath: Uri

    var testing = "test string in Tab Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab)

        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager, currentList)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        tab_layout.setupWithViewPager(container)
        tab_layout.setSelectedTabIndicatorColor(resources.getColor(R.color.white))

        Log.v("TabAct", testing)

        file = File(this@TabActivity.getExternalFilesDir(
            Environment.DIRECTORY_DOCUMENTS), "CacheData") // ~/Documents/CacheData

        filePath = Uri.Builder().appendPath(file.path).appendPath("cache.json").build()


        // look into the file storage on user phone and check if catched file exists
        // if it exists read in the data
        // else create new spot list and save it to storage and read in that file
        // pass read in list to all three fragments??
        var existingFile = File(filePath.path)
        if (existingFile.exists()) {
            // read in the data and set currentList to that list
            val gson = GsonBuilder().setPrettyPrinting().create()

            val bufferedReader = existingFile.bufferedReader()
            val jsonSpotList = bufferedReader.use { it.readText() }

            val type = object : TypeToken<ArrayList<SpotList.Spot>>() {}.type
            val resultList = gson.fromJson<ArrayList<SpotList.Spot>>(jsonSpotList, type)
            Log.v("TabJOY", resultList.toString())
            currentList = resultList
        } else {
            currentList = SpotList.list
        }

    }

    override fun updateCurrentList(currentList: List<SpotList.Spot>) {
        this.currentList = currentList as ArrayList<SpotList.Spot>
        saveFile()
    }

    override fun testDataPassed(testString: String) {
        testing = testString
        Log.v("TabAct: testDataPassed", testing)
        saveFile()
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
        if (!file?.mkdirs()) {
            Log.e("TabActivity", "Directory not created")
            // will throw this message if the directory already exists due to rebuilding the app many times on the phone
        }

        var intentService = Intent(this@TabActivity, SaveIntentService::class.java)
        intentService.putExtra("filePath", filePath.path)
        intentService.putExtra("spotList", currentList)
        startService(intentService)
    }
}
