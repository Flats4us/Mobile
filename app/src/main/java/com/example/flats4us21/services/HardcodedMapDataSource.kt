package com.example.flats4us21.services

import android.graphics.Bitmap
import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.dto.Property
import com.example.flats4us21.data.Owner
import com.example.flats4us21.data.Equipment

class HardcodedMapDataSource : MapDataSource {
    private val offers: List<Offer> = listOf(

    )

    private fun createOwner(id: Int): Owner {
       TODO() }


    private fun createEquipmentList(): List<Equipment> {
        return listOf(
            Equipment(1, "Item1"), // Example, replace with actual equipment details
            Equipment(2, "Item2")  // Example, replace with actual equipment details
        )
    }


    private fun createImageList(): List<Bitmap> {
        // Placeholder for Image list creation logic
        return listOf(Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888))
    }

    override fun getOffers(): List<Offer> {
        return offers
    }
}
