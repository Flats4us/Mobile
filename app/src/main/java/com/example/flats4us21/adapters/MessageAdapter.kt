import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.flats4us21.R
import com.example.flats4us21.data.Message

class MessageAdapter(context: Context, messages: List<Message>) : ArrayAdapter<Message>(context, 0, messages) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val message = getItem(position)

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false)
        }

        val textViewMessage = view?.findViewById<TextView>(R.id.messageText)
        val textViewTime = view?.findViewById<TextView>(R.id.messageTime)

        textViewMessage?.text = message?.text
        textViewTime?.text = message?.time

        return view!!
    }
}
