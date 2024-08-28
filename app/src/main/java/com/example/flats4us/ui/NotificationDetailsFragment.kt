package com.example.flats4us.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us.data.Notification
import com.example.flats4us.data.utils.Constants.Companion.NOTIFICATION_ID
import com.example.flats4us.data.utils.QuestionTranslator
import com.example.flats4us.databinding.FragmentNotificationDetailsBinding
import com.example.flats4us.viewmodels.NotificationViewModel

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
            val unreadListId = notificationViewModel.unreadNotifications.value!!.indexOf(notification)
            notificationViewModel.removeNotification(notification!!)
            Log.i(TAG, "notification $notification")
            Log.i(TAG, "read ${!notification.read}")
            Log.i(TAG, "contains ${!notificationViewModel.notificationIds.contains(notificationId)}")
            Log.i(TAG,
                (!notification.read && !notificationViewModel.notificationIds.contains(notificationId)).toString()
            )
            if(!notification.read && !notificationViewModel.notificationIds.contains(notificationId)) {
                notificationViewModel.notificationIds.add(notificationId)
                notificationViewModel.markNotificationsAsRead()
                notificationViewModel.notifications.value!![listId].read = true
            }
        }


        notificationViewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            Log.i(TAG, "isLoading $isLoading")
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.detailsLayout.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        notificationViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                val resourceId = requireContext().resources.getIdentifier(errorMessage, "string", requireContext().packageName)
                val message = if (resourceId != 0) {
                    requireContext().getString(resourceId)
                } else {
                    errorMessage
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                notificationViewModel.clearErrorMessage()
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