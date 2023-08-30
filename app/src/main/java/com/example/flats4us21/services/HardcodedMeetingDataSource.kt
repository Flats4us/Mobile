package com.example.flats4us21.services

import com.example.flats4us21.R
import com.example.flats4us21.data.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

class HardcodedMeetingDataSource : MeetingDataSource {
    private val meetings : MutableList<Meeting> = mutableListOf()

    init{
        meetings.add(
            Meeting(
                date = LocalDateTime.now(),
                reason = "Obejrzenie mieszkania",
                student = Student(
                    birthDate = LocalDate.of(2000, 1, 1),
                    email = "jan.kowalski@gmail.com",
                    studentNumber = "s22222",
                    name = "Jan",
                    surname = "Kowalski",
                    university = "PJATK"
                ),
                offer = Offer(
                    dateIssue = "21-03-2023",
                    status = "aktywny",
                    price = "%.2f".format(3500.50F),
                    description = "Lorem ipsum",
                    rentalPeriod = "2",
                    interestedPeople = 18,
                    Property(
                        propertyType = PropertyType.FLAT,
                        voivodeship = "Mazowieckie",
                        city = "Warszawa",
                        district = "Bielany",
                        street = "Pruszkowska",
                        buildingNumber = "10A",
                        area = 50,
                        maxResidents = 4,
                        constructionYear = 2010,
                        numberOfRooms = 2,
                        numberOfFloors = 1,
                        equipment = "Sofa",
                        image = mutableListOf(R.drawable.property)
                    )
                ),
                MeetingStatus.CONFIRMED
            )
        )
    }
    override fun getMeetings(date: LocalDate): List<Meeting> {
        return meetings.filter { it.date.toLocalDate() == date }
    }

    override fun getMeetingsOfMonth(month: Month, year: Int): List<Meeting> {
        return meetings.filter { it.date.toLocalDate().month == month && it.date.toLocalDate().year == year }
    }
}