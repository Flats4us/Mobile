import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.flats4us21.R

class DisputeFragment : Fragment() {

    private lateinit var messagesAdapter: ArrayAdapter<String>
    private val messagesList = mutableListOf<String>()

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

        // Initialize the adapter for the ListView with a simple list item layout and the messages list
        messagesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, messagesList)
        listViewMessages.adapter = messagesAdapter

        // Set the onClick listener for the send message button
        buttonSendMessage.setOnClickListener {
            val message = editTextMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                // Add message to the list and notify the adapter
                messagesList.add(message)
                messagesAdapter.notifyDataSetChanged()

                // Clear the input field for new messages
                editTextMessage.setText("")
            }
        }
    }
}
