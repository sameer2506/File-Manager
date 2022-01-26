package com.file.manager.utils

fun convertFileSizeToMB(sizeInBytes: Long): Double{
    return (sizeInBytes.toDouble()) / (1024 * 1024)
}