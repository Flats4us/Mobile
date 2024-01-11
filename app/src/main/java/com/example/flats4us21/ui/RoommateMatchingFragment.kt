package com.example.flats4us21.ui

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
import com.google.android.flexbox.FlexboxLayout

class RoommateMatchingFragment : Fragment() {

    private lateinit var buttonAccept: Button
    private lateinit var buttonReject: Button
    private lateinit var textViewUserName: TextView
    private lateinit var textViewUserUniversity: TextView
    private lateinit var flexboxUserInterests: FlexboxLayout
    private lateinit var imageViewUserPhoto: ImageView

    // Modified user data class to include name, age, university, and interests
    data class User(val name: String, val age: Int, val university: String, val interests: List<String>, val photoResId: Int)

    // Updated list of users with interests as a list of strings
    private val users = listOf(
        User("Tomek", 20, "Uczelnia: UKSW", listOf("Pracowity", "Uprzejmy", "Lubiący porządek"), R.drawable.stud1),
        User("Jola", 22, "Uczelnia: WAT", listOf("Wolontariusz", "Miłośnik zwierząt", "Nocny marek"), R.drawable.stud2),
        User("Viola", 21, "Uczelnia: WUM", listOf("Biegacz", "Weganin", "Fitness"), R.drawable.stud3),
        User("Tomek", 20, "Uczelnia: UKSW", listOf("Pracowity", "Uprzejmy", "Lubiący porządek"), R.drawable.stud1),
        User("Jola", 22, "Uczelnia: WAT", listOf("Wolontariusz", "Miłośnik zwierząt", "Nocny marek"), R.drawable.stud2),
        User("Viola", 21, "Uczelnia: WUM", listOf("Biegacz", "Weganin", "Fitness"), R.drawable.stud3),
        User("Tomek", 20, "Uczelnia: UKSW", listOf("Pracowity", "Uprzejmy", "Lubiący porządek"), R.drawable.stud1),
        User("Jola", 22, "Uczelnia: WAT", listOf("Wolontariusz", "Miłośnik zwierząt", "Nocny marek"), R.drawable.stud2),
        User("Viola", 21, "Uczelnia: WUM", listOf("Biegacz", "Weganin", "Fitness"), R.drawable.stud3),
        User("Tomek", 20, "Uczelnia: UKSW", listOf("Pracowity", "Uprzejmy", "Lubiący porządek"), R.drawable.stud1),
        User("Jola", 22, "Uczelnia: WAT", listOf("Wolontariusz", "Miłośnik zwierząt", "Nocny marek"), R.drawable.stud2),
        User("Viola", 21, "Uczelnia: WUM", listOf("Biegacz", "Weganin", "Fitness"), R.drawable.stud3),


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
        textViewUserName = view.findViewById(R.id.textViewUserName)
        textViewUserUniversity = view.findViewById(R.id.textViewUserUniversity)
        flexboxUserInterests = view.findViewById(R.id.flexboxUserInterests)
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
        showNextUser()
    }

    private fun onSwipeLeft() {
        showNextUser()
    }

    private fun showNextUser() {
        if (currentUserIndex < users.size) {
            val currentUser = users[currentUserIndex]
            textViewUserName.text = "${currentUser.name}, ${currentUser.age}"
            textViewUserUniversity.text = currentUser.university
            displayUserInterests(currentUser.interests)
            imageViewUserPhoto.setImageResource(currentUser.photoResId)
            currentUserIndex++
        } else {
            // Handle end of list
        }
    }

    private fun displayUserInterests(interests: List<String>) {
        flexboxUserInterests.removeAllViews() // Clear all views before adding new tags

        interests.forEach { interest ->
            val tagTextView = LayoutInflater.from(context).inflate(R.layout.tag_item, flexboxUserInterests, false) as TextView
            tagTextView.text = interest
            // Customize this TextView further if needed, such as setting the background, padding, margins, etc.
            flexboxUserInterests.addView(tagTextView)
        }
    }
}
