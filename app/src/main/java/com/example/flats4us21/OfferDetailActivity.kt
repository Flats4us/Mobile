package com.example.flats4us21

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.data.Offer
import com.example.flats4us21.databinding.ActivityOfferDetailBinding
import com.example.flats4us21.viewmodels.MainViewModel

class OfferDetailActivity : AppCompatActivity() {
    private lateinit var viewModel : MainViewModel
    private lateinit var binding : ActivityOfferDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfferDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val offer = intent.extras?.let { viewModel.loadDataFromDb().get(it.getInt("index")) }
        bindOfferData(offer)
    }

    private fun bindOfferData(offer: Offer?) {
        offer ?: return

        binding.image.setImageResource(offer.property.image.get(0))
        binding.dateIssue.text = offer.dateIssue
        binding.price.text = offer.price
        binding.address.text = "${offer.property.city} ${offer.property.street}"
        binding.period.text = offer.rentalPeriod
        binding.maxResidents.text = offer.property.maxResidents.toString()
        binding.equipment.text = offer.property.equipment
        binding.area.text = offer.property.area.toString()
        binding.interestedPeople.text = offer.interestedPeople.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.about -> Toast.makeText(this,"About Selected", Toast.LENGTH_SHORT).show()
            R.id.settings -> Toast.makeText(this,"Settings Selected", Toast.LENGTH_SHORT).show()
            R.id.exit -> Toast.makeText(this,"Exit Selected", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}