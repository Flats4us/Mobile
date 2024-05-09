import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flats4us21.R
import com.example.flats4us21.data.Message
import com.example.flats4us21.databinding.FragmentChatBinding
import java.text.SimpleDateFormat
import java.util.*

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_DISPUTE = "dispute"

        fun newInstance(dispute: Dispute): ChatFragment {
            val fragment = ChatFragment()
            val args = Bundle()
            args.putSerializable(ARG_DISPUTE, dispute)
            fragment.arguments = args
            return fragment
        }
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dispute = arguments?.getSerializable(ARG_DISPUTE) as? Dispute
        dispute?.let {
            binding.disputeTitleTextView.text = it.title
            binding.disputeDescriptionTextView.text = it.description
        }

        val messages = mutableListOf<Message>()
        val adapter = MessageAdapter(messages)
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(context) // Ustawienie LayoutManager
        binding.chatRecyclerView.adapter = adapter

        binding.sendButton.setOnClickListener {
            val messageContent = binding.messageEditText.text.toString()
            if (messageContent.isNotEmpty()) {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.HOUR, 1)
                val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
                val newMessage = Message(messageContent, currentTime)
                messages.add(newMessage)
                adapter.notifyItemInserted(messages.size - 1)
                binding.chatRecyclerView.scrollToPosition(messages.size - 1)
                binding.messageEditText.text.clear()
            }
        }
        setupMenu()

    }

    private fun setupMenu() {
        val moreMenu = binding.menuMore2 // Bind to the ImageView
        moreMenu.setOnClickListener { showPopupMenu(it) }
    }

    private fun showPopupMenu(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(R.menu.chat_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_new_dispute -> {
                    // Handle new dispute action
                    true
                }
                else -> false
            }
        }
        popup.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
