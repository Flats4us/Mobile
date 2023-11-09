package com.example.flats4us21.services

import android.net.Uri
import com.example.flats4us21.data.Offer
import com.example.flats4us21.data.Property
import com.example.flats4us21.data.PropertyType

class HardcodedMapDataSource : MapDataSource {
    private val offers: List<Offer> = listOf(
        Offer("2023-01-01", "Available", "1000$", "Nice flat in central area", "12 months", 5,
            Property(PropertyType.FLAT, "Łódzkie", "Łódź", "Bałuty", "Piotrkowska", "45", 85, 3, 2008, 2, 1, mutableListOf("Parking", "Elevator"), mutableListOf<Uri>())
        ),
        Offer("2023-02-15", "Available", "750$", "Cozy house with garden", "6 months", 3,
            Property(PropertyType.HOUSE, "Podkarpackie", "Rzeszów", "Centrum", "Długa", "7", 200, 8, 2015, 4, 3, mutableListOf("Swimming Pool", "Garden"), mutableListOf<Uri>())
        ),
        Offer("2023-03-10", "Rented", "550$", "Modern flat near park", "12 months", 1,
            Property(PropertyType.FLAT, "Dolnośląskie", "Wrocław", "Krzyki", "Legnicka", "32", 55, 5, 1995, 2, 1, mutableListOf("Garage", "Security"), mutableListOf<Uri>())
        ),
        Offer("2023-04-05", "Available", "480$", "Studio in city center", "1 month", 10,
            Property(PropertyType.FLAT, "Śląskie", "Katowice", "Śródmieście", "Mariacka", "9", 40, 3, 2010, 1, 1, mutableListOf("Furnished", "Gym"), mutableListOf<Uri>())
        ),
        Offer("2023-05-20", "Available", "650$", "Two-bedroom flat with balcony", "12 months", 2,
            Property(PropertyType.FLAT, "Zachodniopomorskie", "Szczecin", "Pomorzany", "Mściwoja", "17", 70, 6, 2000, 3, 2, mutableListOf("Balcony", "Fireplace"), mutableListOf<Uri>())
        ),
        Offer("2023-06-30", "Under negotiation", "1200$", "House with sauna and terrace", "24 months", 4,
            Property(PropertyType.HOUSE, "Lubelskie", "Lublin", "Czuby", "Chodźki", "11", 180, 7, 1998, 4, 2, mutableListOf("Sauna", "Terrace"), mutableListOf<Uri>())
        ),
        Offer("2023-07-12", "Available", "700$", "Comfortable flat in quiet neighborhood", "12 months", 3,
            Property(PropertyType.FLAT, "Opolskie", "Opole", "Piastów", "Oleska", "4", 65, 5, 2012, 2, 1, mutableListOf("Central Heating", "Lift"), mutableListOf<Uri>())
        ),
        Offer("2023-08-23", "Rented", "300$", "Single room in shared apartment", "12 months", 0,
            Property(PropertyType.ROOM, "Świętokrzyskie", "Kielce", "Paderewskiego", "Klonowa", "22", 35, 2, 2021, 1, 1, mutableListOf("Internet", "Cable TV"), mutableListOf<Uri>())
        ),
        Offer("2023-09-15", "Available", "850$", "Spacious flat perfect for families", "12 months", 5,
            Property(PropertyType.FLAT, "Warmińsko-Mazurskie", "Olsztyn", "Dajtki", "Królowej Jadwigi", "8", 75, 4, 2007, 2, 1, mutableListOf("Bicycle Storage", "Courtyard"), mutableListOf<Uri>())
        ),
        Offer("2023-10-01", "Available", "1300$", "Luxury house with private playground", "12 months", 2,
            Property(PropertyType.HOUSE, "Mazowieckie", "Radom", "Borki", "Kwiatowa", "6", 160, 6, 2019, 4, 2, mutableListOf("Playground", "Solar Panels"), mutableListOf<Uri>())
        )
    )

    override fun getOffers(): List<Offer> {
        return offers
    }
}
