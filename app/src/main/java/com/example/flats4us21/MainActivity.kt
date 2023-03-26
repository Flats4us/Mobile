package com.example.flats4us21

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toggle = findViewById<ImageButton>(R.id.passwordToggle)
        val password =  findViewById<EditText>(R.id.textPassword)
        toggle.setOnClickListener{
            setPasswordVisibility(toggle, password)
        }

        val repeatToggle = findViewById<ImageButton>(R.id.passwordRepeatToggle)
        val repeatPassword =  findViewById<EditText>(R.id.textRepeatPassword)
        repeatToggle.setOnClickListener{
            setPasswordVisibility(repeatToggle, repeatPassword)
        }

        val buttonLogin = findViewById<Button>(R.id.buttonRegister)
        buttonLogin.setOnClickListener {
            Toast.makeText(this, "Login button clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.about -> Toast.makeText(this,"About Selected", Toast.LENGTH_SHORT).show()
            R.id.settings -> Toast.makeText(this,"Settings Selected",Toast.LENGTH_SHORT).show()
            R.id.exit -> Toast.makeText(this,"Exit Selected",Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setPasswordVisibility(toggle : ImageButton, password : EditText){
        if(password.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            toggle.setImageResource(R.drawable.baseline_visibility_24)
            password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        }
        else {
            toggle.setImageResource(R.drawable.baseline_visibility_off_24)
            password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
    }
}