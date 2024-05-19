


package com.example.flats4us21

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.flats4us21.deserializer.PropertyDeserializer
import com.example.flats4us21.ui.AddOfferFragment
import com.example.flats4us21.ui.CalendarFragment
import com.example.flats4us21.ui.ITIssueReportFragment
import com.example.flats4us21.ui.LastViewedOffersFragment
import com.example.flats4us21.ui.MapFragment
import com.example.flats4us21.ui.NotificationsFragment
import com.example.flats4us21.ui.OwnerOffersFragment
import com.example.flats4us21.ui.OwnerPropertiesFragment
import com.example.flats4us21.ui.MyProfileFragment
import com.example.flats4us21.ui.SearchFragment
import com.example.flats4us21.ui.SelectedOfferFragment
import com.example.flats4us21.ui.StartScreenFragment
import com.example.flats4us21.ui.WatchedOffersListFragment
import com.example.flats4us21.viewmodels.UserViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

//const val URL = "http://172.21.40.120:5166"
const val URL = "http://172.27.80.1:5166"
private const val TAG = "DrawerActivity"
class DrawerActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var headerView: View
    private lateinit var userRole: String
    private lateinit var viewModel: UserViewModel
    private lateinit var toggle : ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        DataStoreManager.initialize(applicationContext)
        setContentView(R.layout.activity_drawer)
        val resourceId = R.drawable.property
        val defaultBitmap = BitmapFactory.decodeResource(applicationContext.resources, resourceId)
        PropertyDeserializer.setBitmap(defaultBitmap)
        val toolbar : Toolbar = findViewById(R.id.toolbar)
        drawerLayout  = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view)
        headerView = navView.getHeaderView(0)
        val notificationButton : ImageButton = findViewById(R.id.notification_button)
        notificationButton.setOnClickListener {
            replaceFragment(NotificationsFragment())
            drawerLayout.closeDrawers()
        }
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupDrawerContent()

        DataStoreManager.userRole.observe(this) { user ->
            navView.menu.clear()
            userRole = user ?: ""
            when (userRole) {
                "Student" -> navView.inflateMenu(R.menu.student_nav_menu)
                "Owner" -> navView.inflateMenu(R.menu.owner_nav_menu)
                else -> navView.inflateMenu(R.menu.guest_nav_menu)
            }
        }

        viewModel.myProfile.observe(this) { profile ->
            Log.i(TAG, profile.toString())
            if(profile != null){

                val profilePicture = headerView.findViewById<ImageView>(R.id.profilePicture)
                val mail : TextView = headerView.findViewById(R.id.mail)
                val nameAndSurname : TextView = headerView.findViewById(R.id.nameAndSurname)

                Log.i(TAG, "Is not null $profile")
                mail.isVisible = true
                mail.text = profile.email
                nameAndSurname.text = getString(R.string.name_and_surname, profile.name, profile.surname)
                val url = "$URL/${profile.profilePicture.path}"

                profilePicture.load(url) {
                    error(R.drawable.baseline_person_24)
                }
            }

        }
    }

    private fun setupDrawerContent() {
        navView.setNavigationItemSelectedListener { menuItem ->
            selectDrawerItem(menuItem)
            true
        }
        supportFragmentManager.commit {
            add(R.id.frameLayout,StartScreenFragment(), null)
        }
    }

    private fun selectDrawerItem(menuItem: MenuItem) {
        when(menuItem.itemId){
            R.id.nav_start -> replaceFragment(SearchFragment())
            R.id.nav_map -> replaceFragment(MapFragment())
            R.id.nav_settings -> replaceFragment(AddOfferFragment())
        }
        when (userRole) {
            "Student" -> {
                handleStudentAndOwnerMenuItems(menuItem)
                when (menuItem.itemId) {
                    R.id.nav_observed -> replaceFragment(WatchedOffersListFragment())
                    R.id.nav_last_viewed -> replaceFragment(LastViewedOffersFragment())
                    R.id.nav_my_rentals -> Toast.makeText(this, "Clicked Historia płatności", Toast.LENGTH_SHORT).show()
                    R.id.nav_roommates -> Toast.makeText(this, "Clicked Współlokatorzy", Toast.LENGTH_SHORT).show()

                }
            }
            "Owner" -> {
                handleStudentAndOwnerMenuItems(menuItem)
                when (menuItem.itemId) {
                    R.id.nav_my_properties -> replaceFragment(OwnerPropertiesFragment())
                    R.id.nav_my_offers -> replaceFragment(OwnerOffersFragment())
                }
            }
            else -> {
                when (menuItem.itemId) {
                    R.id.nav_login -> replaceFragment(StartScreenFragment())
                }
            }
        }
        drawerLayout.closeDrawers()
    }

    private fun handleStudentAndOwnerMenuItems(menuItem: MenuItem) {
        when(menuItem.itemId){
            R.id.nav_messages -> Toast.makeText(this, "Wiadomości", Toast.LENGTH_SHORT).show()
            R.id.nav_profile -> replaceFragment(MyProfileFragment())
            R.id.reviews -> Toast.makeText(this, "Clicked Opinie", Toast.LENGTH_SHORT).show()
            R.id.nav_his -> Toast.makeText(this, "Clicked Historia płatności", Toast.LENGTH_SHORT).show()
            R.id.nav_method -> Toast.makeText(this, "Clicked Metody płatności", Toast.LENGTH_SHORT).show()
            R.id.nav_rent -> replaceFragment(SelectedOfferFragment())
            R.id.nav_conflicts -> replaceFragment(ITIssueReportFragment())
            R.id.nav_calendar -> replaceFragment(CalendarFragment())
            R.id.nav_contact -> Toast.makeText(this, "Clicked Kontakt", Toast.LENGTH_SHORT).show()
            R.id.nav_logout -> logout()
        }
    }

    fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun logout() {
        // Clear user data from DataStore
        clearUserData()
        val profilePicture = headerView.findViewById<ImageView>(R.id.profilePicture)
        val mail : TextView = headerView.findViewById(R.id.mail)
        val nameAndSurname : TextView = headerView.findViewById(R.id.nameAndSurname)

        profilePicture.setImageResource(R.drawable.baseline_person_24)
        nameAndSurname.text = getString(R.string.guest)
        mail.visibility = View.INVISIBLE
        // Clear the back stack
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        replaceFragment(StartScreenFragment())
    }

    private fun clearUserData() {
        // Clear user data from DataStore
        lifecycleScope.launch {
            DataStoreManager.clearUserData()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}