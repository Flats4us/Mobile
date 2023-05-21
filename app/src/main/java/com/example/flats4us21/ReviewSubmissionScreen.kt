package com.example.flats4us21

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import com.google.android.material.slider.Slider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast

class ReviewSubmissionScreen : AppCompatActivity() {

    private lateinit var profileImage: ImageView
    private lateinit var addOpinionLabel: TextView
    private lateinit var personName: TextView
    private lateinit var starRating: RatingBar
    private lateinit var opinionEditText: EditText
    private lateinit var tag1: CheckBox
    private lateinit var tag2: CheckBox
    private lateinit var tag3: CheckBox
    private lateinit var addButton: Button
    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_submission_screen)

        profileImage = findViewById(R.id.profileImage)
        addOpinionLabel = findViewById(R.id.addOpinionLabel)
        personName = findViewById(R.id.personName)
        starRating = findViewById(R.id.starRating)
        opinionEditText = findViewById(R.id.opinionEditText)
        tag1 = findViewById(R.id.tag1)
        tag2 = findViewById(R.id.tag2)
        tag3 = findViewById(R.id.tag3)
        addButton = findViewById(R.id.addButton)
        cancelButton = findViewById(R.id.cancelButton)

        addButton.setOnClickListener {
            // Handle add button click
            val opinion = opinionEditText.text.toString()
            val rating = starRating.rating
            val tags = mutableListOf<String>()
            if (tag1.isChecked) tags.add("Communicative")
            if (tag2.isChecked) tags.add("Calm")
            if (tag3.isChecked) tags.add("Loud")

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