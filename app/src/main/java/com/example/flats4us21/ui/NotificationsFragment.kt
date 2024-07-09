package com.example.flats4us21.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.adapters.NotificationAdapter
import com.example.flats4us21.data.Notification
import com.example.flats4us21.databinding.FragmentNotificationsBinding
import com.example.flats4us21.viewmodels.NotificationViewModel

private const val TAG = "NotificationsFragment"
const val NOTIFICATION_ID = "NOTIFICATION_ID"

class NotificationsFragment : Fragment() {
    private var _binding : FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: NotificationAdapter
    private lateinit var viewModel: NotificationViewModel
    private val fetchedNotifications: MutableList<Notification> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[NotificationViewModel::class.java]

        recyclerview = binding.notificationRecyclerView

        adapter = NotificationAdapter(fetchedNotifications, requireContext()) { selectedNotification ->
            val bundle = Bundle()
            bundle.putInt(NOTIFICATION_ID, selectedNotification.notificationId)
            val fragment = NotificationDetailsFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }

        recyclerview.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.setReverseLayout(true)
        layoutManager.stackFromEnd = true
        recyclerview.layoutManager = layoutManager
        viewModel.getAllNotifications()

        viewModel.notifications.observe(viewLifecycleOwner) { notifications ->
            Log.i(TAG, "Number of notifications: ${notifications.size}")
            fetchedNotifications.clear()
            fetchedNotifications.addAll(notifications)
            adapter.updateNotifications(fetchedNotifications)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            Log.i(TAG, "isLoading $isLoading")
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            recyclerview.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
