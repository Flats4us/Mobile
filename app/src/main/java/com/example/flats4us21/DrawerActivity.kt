


package com.example.flats4us21

import DisputeFragment
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.flats4us21.deserializer.PropertyDeserializer
import com.example.flats4us21.ui.*
import com.google.android.material.navigation.NavigationView

class   DrawerActivity : AppCompatActivity() {

    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var drawerLayout : DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        val resourceId = R.drawable.property
        val defaultBitmap = BitmapFactory.decodeResource(applicationContext.resources, resourceId)
        PropertyDeserializer.setBitmap(defaultBitmap)
        drawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_start -> replaceFragment(SearchFragment())
                R.id.nav_observed -> replaceFragment(WatchedOffersListFragment())
                R.id.nav_map -> replaceFragment(MapFragment())
                R.id.nav_last_viewed -> replaceFragment(LastViewedOffersFragment())
                R.id.nav_messages -> Toast.makeText(this, "Wiadomości", Toast.LENGTH_SHORT).show()
                R.id.nav_profile -> replaceFragment(ProfileFragment())
                R.id.reviews -> replaceFragment(ReviewSubmissionFragment())
                R.id.nav_settings -> replaceFragment(AddOfferFragment())
                R.id.nav_his -> Toast.makeText(this, "Clicked Historia płatności", Toast.LENGTH_SHORT).show()
                R.id.nav_method -> Toast.makeText(this, "Clicked Metody płatności", Toast.LENGTH_SHORT).show()
                R.id.nav_rent -> Toast.makeText(this, "Clicked Metody płatności", Toast.LENGTH_SHORT).show()
                R.id.nav_my_rentals -> replaceFragment(AddRealEstateFragment())
                R.id.nav_conflicts -> replaceFragment(DisputeFragment())
                R.id.nav_calendar -> replaceFragment(CalendarFragment())
                R.id.nav_logout -> Toast.makeText(this, "Clicked Wyloguj się", Toast.LENGTH_SHORT).show()
            }
            true
        }
        supportFragmentManager.commit {
            add(R.id.frameLayout,StartScreenFragment(), null)
        }
    }

    fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}