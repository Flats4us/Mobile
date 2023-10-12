package com.example.flats4us21

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class ReviewSubmissionFragment : Fragment() {

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_review_submission_screen, container, false)

        userImage = view.findViewById(R.id.userImage)
        addReviewText = view.findViewById(R.id.addReviewText)
        userNameText = view.findViewById(R.id.userNameText)
        starRating = view.findViewById(R.id.starRating)
        reviewEditText = view.findViewById(R.id.reviewEditText)
        tag1 = view.findViewById(R.id.tag1)
        tag2 = view.findViewById(R.id.tag2)
        tag3 = view.findViewById(R.id.tag3)
        addButton = view.findViewById(R.id.addButton)
        cancelButton = view.findViewById(R.id.cancelButton)

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
            // If you want to close the fragment:
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun submitReview(opinion: String, rating: Float, tags: List<String>) {
        // Perform your logic here to handle the submitted review
        // You can access the opinion, rating, and tags and perform necessary actions
        // For example, you can show a toast message indicating the review has been submitted
        Toast.makeText(requireContext(), "Review submitted!", Toast.LENGTH_SHORT).show()
    }
}
