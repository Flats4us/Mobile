package com.example.flats4us21.services

import com.example.flats4us21.data.Property
import com.example.flats4us21.data.PropertyType
import com.example.flats4us21.data.RentalPlace
import org.osmdroid.util.GeoPoint

class HardcodedMapDataSource : MapDataSource {
    private val properties: List<Property> = listOf(
        Property(PropertyType.FLAT, "Łódzkie", "Łódź", "Bałuty", "Piotrkowska", "45", 85, 3, 2008, 2, 1, mutableListOf("Parking", "Elevator"), mutableListOf()),
        Property(PropertyType.HOUSE, "Podkarpackie", "Rzeszów", "Centrum", "Długa", "7", 200, 8, 2015, 4, 3, mutableListOf("Swimming Pool", "Garden"), mutableListOf()),
        Property(PropertyType.FLAT, "Dolnośląskie", "Wrocław", "Krzyki", "Legnicka", "32", 55, 5, 1995, 2, 1, mutableListOf("Garage", "Security"), mutableListOf()),
        Property(PropertyType.FLAT, "Śląskie", "Katowice", "Śródmieście", "Mariacka", "9", 40, 3, 2010, 1, 1, mutableListOf("Furnished", "Gym"), mutableListOf()),
        Property(PropertyType.FLAT, "Zachodniopomorskie", "Szczecin", "Pomorzany", "Mściwoja", "17", 70, 6, 2000, 3, 2, mutableListOf("Balcony", "Fireplace"), mutableListOf()),
        Property(PropertyType.HOUSE, "Lubelskie", "Lublin", "Czuby", "Chodźki", "11", 180, 7, 1998, 4, 2, mutableListOf("Sauna", "Terrace"), mutableListOf()),
        Property(PropertyType.FLAT, "Opolskie", "Opole", "Piastów", "Oleska", "4", 65, 5, 2012, 2, 1, mutableListOf("Central Heating", "Lift"), mutableListOf()),
        Property(PropertyType.ROOM, "Świętokrzyskie", "Kielce", "Paderewskiego", "Klonowa", "22", 35, 2, 2021, 1, 1, mutableListOf("Internet", "Cable TV"), mutableListOf()),
        Property(PropertyType.FLAT, "Warmińsko-Mazurskie", "Olsztyn", "Dajtki", "Królowej Jadwigi", "8", 75, 4, 2007, 2, 1, mutableListOf("Bicycle Storage", "Courtyard"), mutableListOf()),
        Property(PropertyType.HOUSE, "Mazowieckie", "Radom", "Borki", "Kwiatowa", "6", 160, 6, 2019, 4, 2, mutableListOf("Playground", "Solar Panels"), mutableListOf()),
        Property(PropertyType.FLAT, "Małopolskie", "Tarnów", "Krakowska", "Sienkiewicza", "27", 60, 3, 2003, 2, 1, mutableListOf("Intercom", "Pantry"), mutableListOf()),
        Property(PropertyType.HOUSE, "Pomorskie", "Gdynia", "Orłowo", "Żeromskiego", "14", 180, 7, 2010, 4, 2, mutableListOf("Swimming Pool", "Garden"), mutableListOf()),
        Property(PropertyType.FLAT, "Kujawsko-Pomorskie", "Bydgoszcz", "Fordon", "Lecha", "6", 50, 3, 2005, 2, 1, mutableListOf("Parking", "Balcony"), mutableListOf()),
        Property(PropertyType.HOUSE, "Lubuskie", "Zielona Góra", "Centrum", "Sienkiewicza", "3", 220, 9, 2022, 5, 3, mutableListOf("Sauna", "Terrace"), mutableListOf()),
        Property(PropertyType.FLAT, "Śląskie", "Częstochowa", "Dąbie", "Tysiąclecia", "8", 70, 4, 2014, 2, 1, mutableListOf("Central Heating", "Elevator"), mutableListOf()),
        Property(PropertyType.ROOM, "Warmińsko-Mazurskie", "Elbląg", "Centrum", "Kościuszki", "10", 25, 1, 2020, 1, 1, mutableListOf("Internet", "Furnished"), mutableListOf())
        )

    override fun getProperties(): List<Property> {
        return properties
    }
}

