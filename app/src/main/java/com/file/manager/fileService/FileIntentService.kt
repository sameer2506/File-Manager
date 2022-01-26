package com.file.manager.fileService

import android.app.IntentService
import android.content.Intent
import com.file.manager.R
import com.file.manager.utils.log
import java.io.File

class FileIntentService : IntentService("FileIntentService") {

    companion object {
        const val ACTION_COPY: String = "com.file.manager.fileService.copy"
        const val EXTRA_FILE_SOURCE_PATH: String = "com.file.manager.fileService.source_path"
        const val EXTRA_FILE_DESTINATION_PATH: String =
            "com.file.manager.fileService.destination_path"
    }

    override fun onHandleIntent(intent: Intent?) {
        log("Starting service")
        if (intent?.action == ACTION_COPY) {
            if (intent.hasExtra(EXTRA_FILE_SOURCE_PATH) && intent.hasExtra(
                    EXTRA_FILE_DESTINATION_PATH
                )
            ) {
                copyFile(
                    intent.getStringExtra(EXTRA_FILE_SOURCE_PATH)!!,
                    intent.getStringExtra(EXTRA_FILE_DESTINATION_PATH)!!
                )
            }
        }
    }

    private fun copyFile(source: String, destination: String) {
        val sourceFile = File(source)
        var destinationFile =
            File(destination + "/${sourceFile.nameWithoutExtension}-copy.${sourceFile.extension}")

        val counter = 2
        while (destinationFile.exists()) {
            destinationFile =
                File(destination + "/${sourceFile.nameWithoutExtension}-copy ($counter).${sourceFile.extension}")
        }

        sourceFile.copyTo(destinationFile)

        val broadcastIntent = Intent()
        broadcastIntent.action = applicationContext.getString(R.string.file_change_broadcast)
        broadcastIntent.putExtra(FileChangeBroadcastReceiver.EXTRA_PATH, destination)
        sendBroadcast(broadcastIntent)
    }
}