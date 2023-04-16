package com.example.flats4us21

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import com.google.android.material.slider.Slider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView


class SurveyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val scrollView = ScrollView(this)
        setContentView(scrollView)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        scrollView.addView(layout)

        // Nagłówek
        val header = MaterialTextView(this)
        header.text = "Ankieta studenta"
        header.textSize = 24f
        header.textAlignment = MaterialTextView.TEXT_ALIGNMENT_CENTER
        header.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = 32
        }
        layout.addView(header)

        // Pytania 1-4
        val pytania = listOf(
            "Jak często imprezuję?",
            "Jak często sprzątam w mieszkaniu?",
            "Palę papierosy?",
            "Jestem towarzyski?"
        )
        for (pytanie in pytania) {
            val pytanieView = MaterialTextView(this)
            pytanieView.text = pytanie
            pytanieView.textSize = 18f
            pytanieView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 48
            }
            layout.addView(pytanieView)

            val slider = Slider(this)
            slider.valueFrom = 0f
            slider.valueTo = 10f
            slider.stepSize = 1f
            slider.value = 5f
            slider.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layout.addView(slider)
        }

        // Pytania 5-7
        val pytania2 = listOf(
            "Posiadam zwierzę?",
            "Jestem weganinem?",
            "Szukam współlokatora?"
        )
        for (pytanie in pytania2) {
            val pytanieView = MaterialTextView(this)
            pytanieView.text = pytanie
            pytanieView.textSize = 18f
            pytanieView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 48
            }
            layout.addView(pytanieView)

            val toggle = SwitchMaterial(this)
            toggle.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layout.addView(toggle)
        }

        // Pytania 8-10
        val pytania3 = listOf(
            "Jaką liczbę współlokatorów akceptuję?",
            "Minimalny wiek współlokatora",
            "Maksymalny wiek współlokatora"
        )
        for (pytanie in pytania3) {
            val pytanieView = MaterialTextView(this)
            pytanieView.text = pytanie
            pytanieView.textSize = 18f
            pytanieView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 48
            }
            layout.addView(pytanieView)
            val inputLayout = TextInputLayout(this)
            inputLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val inputEditText = TextInputEditText(this)
            inputEditText.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            inputLayout.addView(inputEditText)
            layout.addView(inputLayout)
        }

        // Pytanie 11
        val pytanie11 = MaterialTextView(this)
        pytanie11.text = "Preferowana płeć współlokatora?"
        pytanie11.textSize = 18f
        pytanie11.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = 48
        }
        layout.addView(pytanie11)

        val checkboxLayout = LinearLayout(this)
        checkboxLayout.orientation = LinearLayout.HORIZONTAL
        checkboxLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val kCheckbox = SwitchMaterial(this)
        kCheckbox.text = "K"
        kCheckbox.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        checkboxLayout.addView(kCheckbox)

        val mCheckbox = SwitchMaterial(this)
        mCheckbox.text = "M"
        mCheckbox.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            leftMargin = 48
        }
        checkboxLayout.addView(mCheckbox)

        layout.addView(checkboxLayout)

        val submitButton = Button(this)
        submitButton.text = "Potwierdź"
        submitButton.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = 48
            gravity = LinearLayout.LayoutParams.MATCH_PARENT
        }
        layout.addView(submitButton)
    }
}