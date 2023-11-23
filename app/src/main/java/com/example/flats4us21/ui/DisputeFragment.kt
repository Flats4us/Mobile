import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.flats4us21.R
import com.example.flats4us21.data.Message
import java.text.SimpleDateFormat
import java.util.*

class DisputeFragment : Fragment() {

    private lateinit var messagesAdapter: MessageAdapter
    private val messagesList = mutableListOf<Message>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dispute, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listViewMessages = view.findViewById<ListView>(R.id.messageList)
        val editTextMessage = view.findViewById<EditText>(R.id.messageInput)
        val buttonSendMessage = view.findViewById<Button>(R.id.sendButton)

        // Initialize the custom adapter for the ListView with the message list
        messagesAdapter = MessageAdapter(requireContext(), messagesList)
        listViewMessages.adapter = messagesAdapter

        // Set the onClick listener for the send message button
        buttonSendMessage.setOnClickListener {
            val messageText = editTextMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                // Get current time and add one hour
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.HOUR_OF_DAY, 1)
                val timeOneHourForward = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)

                val message = Message(messageText, timeOneHourForward)

                // Add message to the list and notify the adapter
                messagesList.add(message)
                messagesAdapter.notifyDataSetChanged()

                // Clear the input field for new messages
                editTextMessage.setText("")
            }
        }
    }

}
