


package com.example.flats4us21

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class DrawerActivity : AppCompatActivity() {

    lateinit var toggle : ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_start -> Toast.makeText(this, "Clicked Strona główna", Toast.LENGTH_SHORT).show()
                R.id.nav_observed -> Toast.makeText(this, "Clicked Obserwowane", Toast.LENGTH_SHORT).show()
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
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item)
    }
}