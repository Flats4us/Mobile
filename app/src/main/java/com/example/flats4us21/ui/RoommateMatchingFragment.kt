package com.example.yourapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.flats4us21.R

class RoommateMatchingFragment : Fragment() {

    private lateinit var buttonAccept: Button
    private lateinit var buttonReject: Button
    private lateinit var textViewUserDescription: TextView
    private lateinit var imageViewUserPhoto: ImageView

    // Przykładowa lista użytkowników z opisami i zdjęciami (przykładowe ID zasobów)
    private val users = listOf(
        Pair("User1 - Pracowity, Uprzejmy, Lubiący porządek", R.drawable.stud1),
        Pair("User2 - Student, Miłośnik zwierząt, Nocny marek", R.drawable.stud2),
        Pair("User3 - Zawodowy, Weganin, Fitness", R.drawable.stud3)
    )
    private var currentUserIndex = 0

    private var x1: Float = 0.0f
    private var x2: Float = 0.0f
    private val MIN_DISTANCE = 150

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_roommate_matching, container, false)

        buttonAccept = view.findViewById(R.id.buttonAccept)
        buttonReject = view.findViewById(R.id.buttonReject)
        textViewUserDescription = view.findViewById(R.id.textViewUserDescription)
        imageViewUserPhoto = view.findViewById(R.id.imageViewUserPhoto)

        imageViewUserPhoto.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    x1 = event.x
                    true
                }
                MotionEvent.ACTION_UP -> {
                    x2 = event.x
                    val deltaX = x2 - x1

                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        if (x2 > x1) {
                            onSwipeRight()
                        } else {
                            onSwipeLeft()
                        }
                    }
                    true
                }
                else -> false
            }
        }

        buttonAccept.setOnClickListener {
            onSwipeRight()
        }

        buttonReject.setOnClickListener {
            onSwipeLeft()
        }

        showNextUser()

        return view
    }

    private fun onSwipeRight() {
        // Akcja dla swipe right (akceptacja)
        showNextUser()
    }

    private fun onSwipeLeft() {
        // Akcja dla swipe left (odrzucenie)
        showNextUser()
    }

    private fun showNextUser() {
        if (currentUserIndex < users.size) {
            val currentUser = users[currentUserIndex]
            textViewUserDescription.text = currentUser.first
            imageViewUserPhoto.setImageResource(currentUser.second)
            currentUserIndex++
        } else {
            // Obsługa sytuacji, gdy skończą się użytkownicy (np. wyświetlenie komunikatu)
        }
    }
}
