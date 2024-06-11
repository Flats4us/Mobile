package com.example.flats4us21.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flats4us21.data.Notification
import com.example.flats4us21.databinding.FragmentNotificationDetailsBinding

private const val TAG = "NotificationDetailsFragment"
class NotificationDetailsFragment : Fragment() {
    private var _binding : FragmentNotificationDetailsBinding? = null
    private val binding get() = _binding!!

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
//        notificationViewModel = ViewModelProvider(requireActivity())[DetailNotificationViewModel::class.java]
//        if (notificationId != null) {
//            notificationViewModel.getNotifications(notificationId)
//        }
//
//       notificationViewModel.notification.observe(viewLifecycleOwner) {notification ->
//           bindNotificationData(notification)
//       }
//
//        notificationViewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
//            Log.i(TAG, "isLoading $isLoading")
//            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
//            binding.detailsLayout.visibility = if (isLoading) View.GONE else View.VISIBLE
//        }
//
//        notificationViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
//            if(errorMessage != null) {
//                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
//            }
//        }
    }

    private fun bindNotificationData(notification: Notification?) {
        notification ?: return

        binding.title.text = notification.title
        binding.description.text = notification.body
    }

}