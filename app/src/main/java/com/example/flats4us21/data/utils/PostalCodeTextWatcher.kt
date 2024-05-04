package com.example.flats4us21.data.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class PostalCodeTextWatcher(private val editText: EditText) : TextWatcher {
    private var isFormatting: Boolean = false
    private val textPattern = "##-###"

    override fun afterTextChanged(s: Editable?) {
        if (!isFormatting) {
            isFormatting = true
            formatText(s)
            isFormatting = false
        }
    }

    private fun formatText(s: Editable?) {
        if (s != null && s.isNotEmpty()) {
            val formatted = StringBuilder()
            var patternIndex = 0
            var count = 0
            for (i in 0 until s.length) {
                if (patternIndex < textPattern.length) {
                    if (textPattern[patternIndex] == '#') {
                        if (count < 2) {
                            formatted.append(s[i])
                            count++
                        } else {
                            if(count == 2)
                                formatted.append("-")
                            formatted.append(s[i])
                            count++;
                        }
                    } else {
                        formatted.append(textPattern[patternIndex])
                        patternIndex++
                    }
                    patternIndex++
                }
            }
            editText.removeTextChangedListener(this)
            editText.setText(formatted.toString())
            editText.setSelection(formatted.length)
            editText.addTextChangedListener(this)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}