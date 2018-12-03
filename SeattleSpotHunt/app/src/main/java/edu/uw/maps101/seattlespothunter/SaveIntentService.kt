package edu.uw.maps101.seattlespothunter

import android.app.IntentService
import android.content.Intent
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter

class SaveIntentService : IntentService("SaveIntentService") {

    private val TAG = "AutoSave"

    /**
     * creates/writes a new file to the external storage of the phone
     * The file Uri is being passed in via intent extras
     * This method needs to be tested somehow but I cannot seem to see files
     * on the test phone.
     */
    override fun onHandleIntent(intent: Intent) {
        if (isExternalStorageWritable()) {
            val geoJson = intent.extras.getString("geoJson")
            val file = intent.extras.getString("filePath")
            val newFile = File(file)
            if (newFile.exists()) {
                newFile.delete()
            }

            val fileOutputStream =  FileOutputStream(newFile)
            val print = PrintWriter(fileOutputStream)
            print.println(geoJson)
            print.flush()
            print.close()
        }

        /*
          Not sure if this needs to be here but Joel has it in his code and it kinda makes
          sense to me.
         */
        try {
            Thread.sleep(2000) //sleep for 2 seconds
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }

    /* Checks if external storage is available for read and write */
    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }
}
