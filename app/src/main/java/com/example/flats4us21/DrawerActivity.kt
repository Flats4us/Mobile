


package com.example.flats4us21

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.flats4us21.ui.CalendarFragment
import com.example.flats4us21.ui.SearchFragment
import com.example.flats4us21.ui.StartScreenFragment
import com.google.android.material.navigation.NavigationView

class DrawerActivity : AppCompatActivity() {

    lateinit var toggle : ActionBarDrawerToggle
    lateinit var drawerLayout : DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        drawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_start -> replaceFragment(SearchFragment())
                R.id.nav_observed -> replaceFragment(CalendarFragment())
                R.id.nav_map -> Toast.makeText(this, "Clicked Mapa ofert", Toast.LENGTH_SHORT).show()
                R.id.nav_messages -> Toast.makeText(this, "Wiadomości", Toast.LENGTH_SHORT).show()
                R.id.nav_profile -> Toast.makeText(this, "Clicked Konto", Toast.LENGTH_SHORT).show()
                R.id.nav_settings -> Toast.makeText(this, "Clicked Ustawienia", Toast.LENGTH_SHORT).show()
                R.id.nav_his -> Toast.makeText(this, "Clicked Historia płatności", Toast.LENGTH_SHORT).show()
                R.id.nav_method -> Toast.makeText(this, "Clicked Metody płatności", Toast.LENGTH_SHORT).show()
                R.id.nav_rent -> Toast.makeText(this, "Clicked opłaty", Toast.LENGTH_SHORT).show()
                R.id.nav_logout -> Toast.makeText(this, "Clicked Wyloguj się", Toast.LENGTH_SHORT).show()
            }
            true
        }
        supportFragmentManager.commit {
            add(R.id.frameLayout,StartScreenFragment(), null)
        }
    }

    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item)
    }
}