package com.file.manager.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.file.manager.model.FileModel
import java.io.File

fun convertFileSizeToMB(sizeInBytes: Long): Double{
    return (sizeInBytes.toDouble()) / (1024 * 1024)
}

fun Context.launchFileIntent(fileModel: FileModel){
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = FileProvider.getUriForFile(this, packageName, File(fileModel.path))
    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION.or(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    startActivity(Intent.createChooser(intent, "Select Application"))
}