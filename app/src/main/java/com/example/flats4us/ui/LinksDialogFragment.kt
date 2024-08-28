package com.example.flats4us.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flats4us.R
import com.example.flats4us.adapters.LinksAdapter
import com.example.flats4us.databinding.FragmentLinksDialogBinding


class LinksDialogFragment(private val addedLinks: MutableList<String>) : DialogFragment() {
    private var links : MutableList<String> = mutableListOf()
    private lateinit var binding: FragmentLinksDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            links.addAll(addedLinks)
            val builder = AlertDialog.Builder(it)
            binding = FragmentLinksDialogBinding.inflate(layoutInflater)

            val adapter = LinksAdapter(links)

            binding.linksRecyclerView.adapter = adapter
            binding.linksRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            binding.addLinkButton.setOnClickListener {
                if(validateData()){
                    links.add(binding.linkEditText.text.toString())
                    binding.linkEditText.text.clear()
                    adapter.notifyItemInserted(links.lastIndex)
                } else {
                    Toast.makeText(requireContext(),
                        getString(R.string.wrong_link), Toast.LENGTH_LONG).show()
                }
            }

            builder
                .setTitle(getString(R.string.add_link))
                .setCancelable(false)
                .setView(binding.root)
                .setPositiveButton(getString(R.string.accept)) { _, _ ->
                    addedLinks.removeAll(addedLinks)
                    addedLinks.addAll(links)
                }
                .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                    dialog.dismiss()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun validateData(): Boolean {
        return isLinkValid(binding.linkEditText, binding.layoutLink)
    }

    private fun isLinkValid(editText: EditText, editTextLayout: ViewGroup): Boolean {
        val link = editText.text.toString()
        val isNotEmpty = link.isNotEmpty()
        val linkPattern = Regex("""^(http://www\.|https://www\.|http://|https://)?[a-z\d]+([\-.][a-z\d]+)*\.[a-z]{2,5}(:\d{1,5})?(/.*)?$""", RegexOption.IGNORE_CASE)
        val isLinkValid = linkPattern.matchEntire(link)?.value == link
        val isValid = isNotEmpty && isLinkValid

        editTextLayout.setBackgroundResource(if (isValid) R.drawable.background_input else R.drawable.background_wrong_input)
        return isValid
    }

}