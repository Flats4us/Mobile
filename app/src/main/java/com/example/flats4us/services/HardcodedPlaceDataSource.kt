package com.example.flats4us.services


class HardcodedPlaceDataSource : PlaceDataSource {
    private val citiesWithDistricts: MutableMap<String, MutableList<String>> = mutableMapOf(
        "Warszawa" to mutableListOf(
            "Bemowo",
            "Białołęka",
            "Bielany",
            "Mokotów",
            "Ochota",
            "Praga Południe",
            "Praga Północ",
            "Rembertów",
            "Śródmieście",
            "Targówek",
            "Ursus",
            "Ursynów",
            "Wawer",
            "Wesoła",
            "Wilanów",
            "Włochy",
            "Wola",
            "Żoliborz"
        ),
        "Gdańsk" to mutableListOf(
            "Aniołki",
            "Brętowo",
            "Brzeźno",
            "Chełm",
            "Jasień",
            "Kokoszki",
            "Krakowiec-Górki Zachodnie",
            "Letnica",
            "Matarnia",
            "Młyniska",
            "Nowy Port",
            "Oliwa",
            "Olszynka",
            "Orunia-Św. Wojciech-Lipce",
            "Osowa",
            "Piecki-Migowo",
            "Przeróbka",
            "Przymorze Małe",
            "Przymorze Wielkie",
            "Rudniki",
            "Siedlce",
            "Stogi",
            "Strzyża",
            "Suchanino",
            "Śródmieście",
            "Ujeścisko-Łostowice",
            "VII Dwór",
            "Wrzeszcz Dolny",
            "Wrzeszcz Górny",
            "Wyspa Sobieszewska",
            "Wzgórze Mickiewicza",
            "Zaspa-Młyniec",
            "Zaspa-Rozstaje",
            "Żabianka-Wejhera-Jelitkowo-Tysiąclecia"
        ),
        "Kraków" to mutableListOf(
            "Stare Miasto",
            "Grzegórzki",
            "Prądnik Czerwony",
            "Prądnik Biały",
            "Krowodrza",
            "Bronowice",
            "Zawierzyniec",
            "Dębniki",
            "Łagiewniki-Borek Fałęcki",
            "Swoszowice",
            "Podgórze Duchackie",
            "Bieżanów-Prokocim",
            "Podgórze",
            "Czyżyny",
            "Mistrzejowice",
            "Bieńczyce",
            "Wzgórze Krzesławickie",
            "Nowa Huta"
        )
    )


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

    override fun getDistricts(city: String): MutableList<String> {
        return if(citiesWithDistricts.containsKey(city)){
            citiesWithDistricts[city]!!
        } else {
            mutableListOf()
        }
    }
}
