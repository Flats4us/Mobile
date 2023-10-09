package com.example.flats4us21.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Filter
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.flats4us21.R
import java.util.*

class DropdownCheckboxAdapter(
    context: Fragment,
    private val items: List<String>,
    private val selectedItems: MutableList<Boolean>
) : ArrayAdapter<String>(context.requireContext(), R.layout.item_dropdown_checkbox, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_dropdown_checkbox, parent, false)

        val checkbox = view.findViewById<CheckBox>(R.id.checkbox)
        val textView = view.findViewById<TextView>(R.id.textView)

        val item = getItem(position)
        textView.text = item

        if (position < selectedItems.size) {
            checkbox.isChecked = selectedItems[position]
        }

        checkbox.setOnClickListener {
            if (position < selectedItems.size) {
                selectedItems[position] = checkbox.isChecked
            }
        }

        return view
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()

                if (constraint != null) {
                    val suggestions = mutableListOf<String>()

                    for (i in items.indices) {
                        if (items[i].lowercase(Locale.getDefault())
                                .contains(constraint.toString().lowercase(Locale.getDefault()))
                        ) {
                            suggestions.add(items[i])
                        }
                    }

                    filterResults.values = suggestions
                    filterResults.count = suggestions.size
                }

                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                clear()
                if (results != null && results.count > 0) {
                    val filteredItems = results.values as List<String>
                    addAll(filteredItems)
                } else {
                    addAll(items)
                }
                notifyDataSetChanged()
            }
        }
    }

    fun setSelectedItems(selectedItems: List<Boolean>) {
        this.selectedItems.clear()
        this.selectedItems.addAll(selectedItems)
        notifyDataSetChanged()
    }

    fun getSelectedItems(): MutableList<String> {
        val selectedStrings = mutableListOf<String>()

        for (i in items.indices) {
            if (i < selectedItems.size && selectedItems[i]) {
                val item = items[i]
                selectedStrings.add(item)
            }
        }

        return selectedStrings
    }
}