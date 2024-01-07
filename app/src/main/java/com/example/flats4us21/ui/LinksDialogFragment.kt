package com.example.flats4us21.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flats4us21.adapters.LinksAdapter
import com.example.flats4us21.databinding.FragmentLinksDialogBinding


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
                Log.i("Test", "Links dialog test!")
                links.add(binding.linkEditText.text.toString())
                binding.linkEditText.text.clear()
                adapter.notifyItemInserted(links.lastIndex)
            }

            builder
                .setTitle("Podaj linki")
                .setCancelable(false)
                .setView(binding.root)
                .setPositiveButton("Akceptuj") { _, _ ->
                    addedLinks.removeAll(addedLinks)
                    addedLinks.addAll(links)
                }
                .setNegativeButton("Anuluj") { dialog, _ ->
                    dialog.dismiss()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}