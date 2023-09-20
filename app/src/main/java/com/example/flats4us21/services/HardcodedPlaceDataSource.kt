package com.example.flats4us21.services


class HardcodedPlaceDataSource : PlaceDataSource {
    override fun getVoivodeships(): List<String> {
        return listOf(
            "Dolnośląskie",
            "Kujawsko-Pomorskie",
            "Lubelskie",
            "Lubuskie",
            "Łódzkie",
            "Małopolskie",
            "Mazowieckie",
            "Opolskie",
            "Podkarpackie",
            "Podlaskie",
            "Pomorskie",
            "Śląskie",
            "Świętokrzyskie",
            "Warmińsko-Mazurskie",
            "Wielkopolskie",
            "Zachodniopomorskie"
        )
    }
}
