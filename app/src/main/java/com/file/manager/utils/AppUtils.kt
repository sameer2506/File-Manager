package com.file.manager.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

fun Context.toast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun log(message: String){
    Log.d("file_manager_log", message)
}