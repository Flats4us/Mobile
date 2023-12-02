package com.example.flats4us21.services

import com.example.flats4us21.data.Meeting
import java.time.LocalDate
import java.time.Month

class HardcodedMeetingDataSource : MeetingDataSource {
    private val meetings : MutableList<Meeting> = mutableListOf()

    /*init{
        meetings.add(
            Meeting(
                date = LocalDateTime.now(),
                reason = "Obejrzenie mieszkania",
                student = Student(
                    id = 1,
                    name = "Jan",
                    surname = "Kowalski",
                    email = "jan.kowalski@gmail.com",
                    phoneNumber = "123456789",
                    profilePicture = null,
                    userStatus = "aktywny",
                    verificationStatus = "zweryfikowany",
                    birthDate = Year.of(2000),
                    studentNumber = "s22222",
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
                        propertyId = 1234,
                        area = 50,
                        buildingNumber = "10A",
                        city = "Warszawa",
                        constructionYear = 2010,
                        district = "Bielany",
                        equipment = mutableListOf("Sofa"),
                        flatNumber = "212",
                        floor = "3",
                        propertyType = PropertyType.FLAT,
                        voivodeship = "Mazowieckie",
                        image = mutableListOf(),

                        street = "Pruszkowska",
                        maxResidents = 4,

                        numberOfRooms = 2,
                        numberOfFloors = 1,
                    )
                ),
                MeetingStatus.CONFIRMED
            )
        )
    }*/
    override fun getMeetings(date: LocalDate): List<Meeting> {
        return meetings.filter { it.date.toLocalDate() == date }
    }

    override fun getMeetingsOfMonth(month: Month, year: Int): List<Meeting> {
        return meetings.filter { it.date.toLocalDate().month == month && it.date.toLocalDate().year == year }
    }
}