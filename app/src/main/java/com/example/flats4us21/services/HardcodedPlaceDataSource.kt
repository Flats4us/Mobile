package com.example.flats4us21.services


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
        "Kraków" to mutableListOf(
            "Stare Miasto",
            "Kazimierz",
            "Podgórze",
            "Krowodrza",
            "Bronowice",
            "Nowa Huta",
            "Bieńczyce",
            "Czyżyny",
            "Zwierzyniec"
        ),
        "Wrocław" to mutableListOf(
            "Stare Miasto",
            "Krzyki",
            "Fabryczna",
            "Psie Pole",
            "Śródmieście",
            "Ołbin",
            "Sępolno",
            "Nadodrze",
            "Brochów",
            "Gaj"
        ),
        "Poznań" to mutableListOf(
            "Śródmieście",
            "Jeżyce",
            "Grunwald",
            "Wilda",
            "Stare Miasto",
            "Nowe Miasto",
            "Winogrady",
            "Naramowice",
            "Antoninek",
            "Umultowo"
        ),
        "Łódź" to mutableListOf(
            "Śródmieście",
            "Bałuty",
            "Widzew",
            "Polesie",
            "Górna",
            "Retkinia",
            "Teofilów",
            "Radogoszcz",
            "Górna"
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
