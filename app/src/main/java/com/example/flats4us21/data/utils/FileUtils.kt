package com.example.flats4us21.data.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider.getUriForFile
import androidx.core.net.toUri
import java.io.File

@SuppressLint("StaticFieldLeak")
object FileUtils {
    private lateinit var context : Context
    fun setContext(context: Context){
        this.context = context
    }
    fun getUriOfExamplePropertyJpg(): Uri {
        /*val imagePath: File = File(context.filesDir, "images")
        val newFile = File(imagePath, "property.jpg")
        val contentUri: Uri = getUriForFile(context, "com.example.fileprovider", newFile)
        return contentUri*/
        val uri: String = "content://com.android.providers.downloads.documents/document/raw%3A%2Fstorage%2Femulated%2F0%2FDownload%2Fproperty.jpg"
        return uri.toUri()
    }
}