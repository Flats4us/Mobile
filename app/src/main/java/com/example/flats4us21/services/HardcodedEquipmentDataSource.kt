package com.example.flats4us21.services

class HardcodedEquipmentDataSource: EquipmentDataSource {
    private val equipment = mutableListOf<String>()

    init {
        equipment.add("Telewizor")
        equipment.add("Lodówka")
        equipment.add("Kuchenka mikrofalowa")
        equipment.add("Pralka")
        equipment.add("Suszarka do ubrań")
        equipment.add("Odkurzacz")
        equipment.add("Zmywarka")
        equipment.add("Klimatyzacja")
        equipment.add("Ekspres do kawy")
        equipment.add("Czajnik elektryczny")
        equipment.add("Mikser")
        equipment.add("Żelazko")
        equipment.add("Suszarka do włosów")
        equipment.add("Ogrzewanie")
        equipment.add("Lampa sufitowa")
        equipment.add("Komputer")
        equipment.add("Biurko")
        equipment.add("Krzesło")
        equipment.add("Sofa")
        equipment.add("Stół jadalniany")
    }

    override fun getEquipment(): MutableList<String> {
        return equipment
    }
}