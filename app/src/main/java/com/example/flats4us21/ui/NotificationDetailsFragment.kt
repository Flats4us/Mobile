package com.example.flats4us21.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.data.Notification
import com.example.flats4us21.data.utils.QuestionTranslator
import com.example.flats4us21.databinding.FragmentNotificationDetailsBinding
import com.example.flats4us21.viewmodels.NotificationViewModel

private const val TAG = "NotificationDetailsFragment"
class NotificationDetailsFragment : Fragment() {
    private var _binding : FragmentNotificationDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var notificationViewModel: NotificationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val notificationId = arguments?.getInt(NOTIFICATION_ID, -1)
        notificationViewModel = ViewModelProvider(requireActivity())[NotificationViewModel::class.java]
        if (notificationId != null) {
            val notification = notificationViewModel.notifications.value!!.firstOrNull { it.notificationId == notificationId }
            bindNotificationData(notification)
            val listId = notificationViewModel.notifications.value!!.indexOf(notification)
            notificationViewModel.notifications.value!![listId].read = true
            notificationViewModel.notificationIds.add(notificationId)
        }


        notificationViewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            Log.i(TAG, "isLoading $isLoading")
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.detailsLayout.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        notificationViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if(errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun bindNotificationData(notification: Notification?) {
        notification ?: return

        binding.title.text = QuestionTranslator.translateNotification(notification.title, requireContext())
        binding.description.text = QuestionTranslator.translateNotification(notification.body, requireContext())
        binding.time.text = notification.dateTime.split("T")[0]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}