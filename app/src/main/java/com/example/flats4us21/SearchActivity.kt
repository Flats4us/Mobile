package com.example.flats4us21

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.adapters.PropertyAdapter
import com.example.flats4us21.data.Offer
import com.example.flats4us21.databinding.ActivitySearchBinding
import com.example.flats4us21.viewmodels.MainViewModel

class SearchActivity : AppCompatActivity() {
    private lateinit var recyclerview : RecyclerView
    private lateinit var adapter: PropertyAdapter
    private lateinit var viewModel : MainViewModel
    private lateinit var binding : ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        recyclerview = binding.propertyRecyclerView
        val offers = viewModel.loadDataFromDb()
        val onUserClick = {offer : Offer ->
            viewModel.setOffer(offer)
            val intent = Intent(this, OfferDetailActivity::class.java)
            intent.putExtra("index", viewModel.getOffer())
            startActivity(intent)
        }
        adapter = PropertyAdapter(offers, onUserClick)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.d("Menu", "onCreateOptionsMenu() called")
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("Menu", "onOptionsItemSelected() called, item id: ${item.itemId}")
        when (item.itemId){
            R.id.about -> Toast.makeText(this,"About Selected", Toast.LENGTH_SHORT).show()
            R.id.settings -> Toast.makeText(this,"Settings Selected", Toast.LENGTH_SHORT).show()
            R.id.exit -> Toast.makeText(this,"Exit Selected", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}
