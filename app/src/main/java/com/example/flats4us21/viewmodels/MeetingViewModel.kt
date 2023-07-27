package com.example.flats4us21.viewmodels

import androidx.lifecycle.ViewModel
import com.example.flats4us21.R
import com.example.flats4us21.data.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import kotlin.streams.toList

private val meeting1 = Meeting(
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
            city = "Warszawa",
            street = "Pruszkowska 10A",
            maxResidents = 4,
            equipment = "Sofa",
            area = 50,
            image = listOf(R.drawable.property),
            numberOfRooms = 2
        )
    ),
    MeetingStatus.CONFIRMED
)
private val data = listOf(meeting1)

class MeetingViewModel : ViewModel() {
    fun getMeetings(date: LocalDate) : List<Meeting> {
        return data.stream().filter { it.date.toLocalDate() == date }.toList()
    }
    fun getMeetingsOfMonth(month : Month, year: Int) : List<Meeting> {
        return data.stream().filter {it.date.toLocalDate().month == month}.filter { it.date.toLocalDate().year == year }.toList()
    }
}