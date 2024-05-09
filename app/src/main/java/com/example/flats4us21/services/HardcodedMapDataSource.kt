package com.example.flats4us21.services

import android.graphics.Bitmap
import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.dto.Property
import com.example.flats4us21.data.Owner
import com.example.flats4us21.data.Equipment

class HardcodedMapDataSource : MapDataSource {
    private val offers: List<Offer> = listOf(
        Offer(1, "2023-01-01", "Available", "1000$", "Nice flat in central area", "12 months", 5, null,
            Property(101, createOwner(1), 85, "45", "Łódź", 2008, "Bałuty", createEquipmentList(), createImageList(), 1, 3, "Piotrkowska", "Łódzkie")
        ),
        Offer(2, "2023-02-15", "Available", "750$", "Cozy house with garden", "6 months", 3, null,
            Property(102, createOwner(2), 200, "7", "Rzeszów", 2015, "Centrum", createEquipmentList(), createImageList(), 3, 8, "Długa", "Podkarpackie")
        ),
        Offer(3, "2023-03-10", "Rented", "550$", "Modern flat near park", "12 months", 1, null,
            Property(103, createOwner(3), 55, "32", "Wrocław", 1995, "Krzyki", createEquipmentList(), createImageList(), 1, 5, "Legnicka", "Dolnośląskie")
        ),
        Offer(4, "2023-04-05", "Available", "480$", "Studio in city center", "1 month", 10, null,
            Property(104, createOwner(4), 40, "9", "Katowice", 2010, "Śródmieście", createEquipmentList(), createImageList(), 1, 3, "Mariacka", "Śląskie")
        ),
        Offer(5, "2023-05-20", "Available", "650$", "Two-bedroom flat with balcony", "12 months", 2, null,
            Property(105, createOwner(5), 70, "17", "Szczecin", 2000, "Pomorzany", createEquipmentList(), createImageList(), 2, 6, "Mściwoja", "Zachodniopomorskie")
        ),
        Offer(6, "2023-06-30", "Under negotiation", "1200$", "House with sauna and terrace", "24 months", 4, null,
            Property(106, createOwner(6), 180, "11", "Lublin", 1998, "Czuby", createEquipmentList(), createImageList(), 2, 7, "Chodźki", "Lubelskie")
        ),
        Offer(7, "2023-07-12", "Available", "700$", "Comfortable flat in quiet neighborhood", "12 months", 3, null,
            Property(107, createOwner(7), 65, "4", "Opole", 2012, "Piastów", createEquipmentList(), createImageList(), 1, 5, "Oleska", "Opolskie")
        ),
        Offer(8, "2023-08-23", "Rented", "300$", "Single room in shared apartment", "12 months", 0, null,
            Property(108, createOwner(8), 35, "22", "Kielce", 2021, "Paderewskiego", createEquipmentList(), createImageList(), 1, 2, "Klonowa", "Świętokrzyskie")
        ),
        Offer(9, "2023-09-15", "Available", "850$", "Spacious flat perfect for families", "12 months", 5, null,
            Property(109, createOwner(9), 75, "8", "Olsztyn", 2007, "Dajtki", createEquipmentList(), createImageList(), 1, 4, "Królowej Jadwigi", "Warmińsko-Mazurskie")
        ),
        Offer(10, "2023-10-01", "Available", "1300$", "Luxury house with private playground", "12 months", 2, null,
            Property(110, createOwner(10), 160, "6", "Radom", 2019, "Borki", createEquipmentList(), createImageList(), 2, 6, "Kwiatowa", "Mazowieckie")
        )
    )

    private fun createOwner(id: Int): Owner {
        // Placeholder for Owner creation logic
        return Owner(id, "Name", "Surname", "email@example.com", "1234567890", null, "Active", "Verified")
    }

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
