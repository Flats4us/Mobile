package com.example.flats4us21.data.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

object UriConverter {
    fun convertUriToBitmap(selectedPhotoUri: Uri, context: Context): Bitmap {
        val bitmap = when {
            Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                context.contentResolver,
                selectedPhotoUri
            )
            else -> {
                val source = ImageDecoder.createSource(context.contentResolver, selectedPhotoUri)
                ImageDecoder.decodeBitmap(source)
            }
        }
        return bitmap
    }
    fun convertUrisToBitmaps(uris: List<Uri>, context: Context): MutableList<Bitmap> {
        val bitmaps: MutableList<Bitmap> = mutableListOf()
        for(j in uris.indices){
            bitmaps.add(convertUriToBitmap(uris[j], context))
        }
        return bitmaps
    }
}