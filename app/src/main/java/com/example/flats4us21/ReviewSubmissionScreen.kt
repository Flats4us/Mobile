package com.example.flats4us21

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast

//TODO: Zmienić na fragment

class ReviewSubmissionScreen : AppCompatActivity() {

    private lateinit var userImage: ImageView
    private lateinit var addReviewText: TextView
    private lateinit var userNameText: TextView
    private lateinit var starRating: RatingBar
    private lateinit var reviewEditText: EditText
    private lateinit var tag1: TextView
    private lateinit var tag2: TextView
    private lateinit var tag3: TextView
    private lateinit var addButton: Button
    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_submission_screen)

        userImage = findViewById(R.id.userImage)
//        addReviewText = findViewById(R.id.addReviewText)
        userNameText = findViewById(R.id.userNameText)
        starRating = findViewById(R.id.starRating)
        reviewEditText = findViewById(R.id.reviewEditText)
        tag1 = findViewById(R.id.tag1)
        tag2 = findViewById(R.id.tag2)
        tag3 = findViewById(R.id.tag2)
//        addButton = findViewById(R.id.addButton)
//        cancelButton = findViewById(R.id.cancelButton)

        // Assuming that when the tag TextViews are clicked, they change color or some visual indication
        // of being selected, you can add click listeners to handle the tag selection here.
        // However, for simplicity, I'm leaving that out in this code.

        addButton.setOnClickListener {
            // Handle add button click
            val opinion = reviewEditText.text.toString()
            val rating = starRating.rating
            val tags = mutableListOf<String>()
            // Note: Instead of checking if a checkbox is checked, you might want to check
            // if a tag TextView has a certain background or text color indicating it's selected.
            // For simplicity, this part is omitted.

            // Perform action with the review data
            submitReview(opinion, rating, tags)
        }

        cancelButton.setOnClickListener {
            // Handle cancel button click
            finish()
        }
    }

    private fun submitReview(opinion: String, rating: Float, tags: List<String>) {
        // Perform your logic here to handle the submitted review
        // You can access the opinion, rating, and tags and perform necessary actions
        // For example, you can show a toast message indicating the review has been submitted
        Toast.makeText(this, "Review submitted!", Toast.LENGTH_SHORT).show()
        finish()
    }
}
