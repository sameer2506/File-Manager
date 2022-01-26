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

fun createNewFile(fileName: String, path: String, callback:(result: Boolean, message: String) -> Unit){
    val fileAlreadyExists = File(path).listFiles().map { it.name }.contains(fileName)
    if (fileAlreadyExists){
        callback(false, "$fileName already exists.")
    }
    else{
        val file = File(path, fileName)
        try{
            val result = file.createNewFile()
            if (result)
                callback(result, "File $fileName created successfully.")
            else
                callback(result, "Unable to create file $fileName.")
        }catch (e: Exception){
            callback(false, "Unable to create file. Please try again.")
            log(e.localizedMessage!!)
        }
    }
}

fun createNewFolder(folderName: String, path: String, callback:(result: Boolean, message: String) -> Unit){
    val fileAlreadyExists = File(path).listFiles().map { it.name }.contains(folderName)
    if (fileAlreadyExists){
        callback(false, "$folderName already exists.")
    }
    else{
        val file = File(path, folderName)
        try{
            val result = file.mkdir()
            if (result)
                callback(result, "File $folderName created successfully.")
            else
                callback(result, "Unable to create file $folderName.")
        }catch (e: Exception){
            callback(false, "Unable to create file. Please try again.")
            log(e.localizedMessage!!)
        }
    }
}


fun deleteFile(path: String){
    val file = File(path)
    if (file.isDirectory)
        file.deleteRecursively()
    else
        file.delete()
}