package com.example.flats4us21

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileInsightActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profileinsight)

        // Przypisanie wartości do elementów interfejsu użytkownika
        val profilePicture = findViewById<ImageView>(R.id.profile_picture)
        profilePicture.setImageResource(R.drawable.baseline_account_box_24)

//        val nameTextView = findViewById<TextView>(R.id.name_textview)
//        nameTextView.text = "Anna"
//
//        val ageTextView = findViewById<TextView>(R.id.age_textview)
//        ageTextView.text = "28"

        val star1 = findViewById<ImageView>(R.id.star_1)
        star1.setImageResource(R.drawable.baseline_star_24)

        val star2 = findViewById<ImageView>(R.id.star_2)
        star2.setImageResource(R.drawable.baseline_star_24)

        val star3 = findViewById<ImageView>(R.id.star_3)
        star3.setImageResource(R.drawable.baseline_star_24)

        val star4 = findViewById<ImageView>(R.id.star_4)
        star4.setImageResource(R.drawable.baseline_star_24)

        val star5 = findViewById<ImageView>(R.id.star_5)
        star5.setImageResource(R.drawable.baseline_star_outline_24)

        val badgeDrinking = findViewById<TextView>(R.id.badge_drinking)
        badgeDrinking.setBackgroundResource(R.drawable.baseline_no_drinks_24)

        val badgeFacebook = findViewById<TextView>(R.id.badge_facebook)
        badgeFacebook.setBackgroundResource(R.drawable.baseline_shower_24)

        val descriptionTextView = findViewById<TextView>(R.id.description_textview)
        descriptionTextView.text =
            "Jestem osobą ożywczą i entuzjastyczną, która ...(CZYTAJ WIĘCEJ)"

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.selectedItemId = R.id.profile_picture

//        bottomNavigationView.setOnNavigationItemSelectedListener {
//            when (it.itemId) {
//                R.id.action_home -> {
//                    startActivity(Intent(this, MainActivity::class.java))
//                    true
//                }
//
//                R.id.action_profile -> {
//                    true
//                }
//
//                R.id.action_messages -> {
//                    startActivity(Intent(this, MessagesActivity::class.java))
//                    true
//                }
//
//                else -> false
//            }
//        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_profile, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
}