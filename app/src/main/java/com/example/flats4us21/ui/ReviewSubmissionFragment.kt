package com.example.flats4us21

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class ReviewSubmissionFragment : Fragment() {

    private lateinit var userImage: ImageView
    private lateinit var addReviewText: TextView
    private lateinit var userNameText: TextView
    private lateinit var starRating: RatingBar
    private lateinit var reviewEditText: EditText
    private lateinit var tag1: CheckBox
    private lateinit var tag2: CheckBox
    private lateinit var tagNeg1: CheckBox
    private lateinit var tagNeg2: CheckBox
    private lateinit var addButton: Button
    private lateinit var cancelButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_review_submission_screen, container, false)

        userImage = view.findViewById(R.id.userImage)
//        addReviewText = view.findViewById(R.id.addReviewText)
        userNameText = view.findViewById(R.id.userNameText)
        starRating = view.findViewById(R.id.starRating)
        reviewEditText = view.findViewById(R.id.reviewEditText)
        tag1 = view.findViewById(R.id.tag1)
        tag2 = view.findViewById(R.id.tag2)
        tagNeg1 = view.findViewById(R.id.tagNeg1)
        tagNeg2 = view.findViewById(R.id.tagNeg2)
        addButton = view.findViewById(R.id.addButton)
        cancelButton = view.findViewById(R.id.cancelButton)

        addButton.setOnClickListener {
            // Handle add button click
            val opinion = reviewEditText.text.toString()
            val rating = starRating.rating
            val tags = mutableListOf<String>()

            if(tag1.isChecked) tags.add(tag1.text.toString())
            if(tag2.isChecked) tags.add(tag2.text.toString())
            if(tagNeg1.isChecked) tags.add(tagNeg1.text.toString())
            if(tagNeg2.isChecked) tags.add(tagNeg2.text.toString())

            // Perform action with the review data
            submitReview(opinion, rating, tags)
        }

        cancelButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun submitReview(opinion: String, rating: Float, tags: List<String>) {
        // Perform your logic here to handle the submitted review
        // You can access the opinion, rating, and tags and perform necessary actions
        Toast.makeText(requireContext(), "Review submitted!", Toast.LENGTH_SHORT).show()
    }
}
