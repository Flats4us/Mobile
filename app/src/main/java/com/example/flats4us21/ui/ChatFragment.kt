import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flats4us21.databinding.FragmentChatBinding

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
            // Here you can display the title and description of the dispute
            // and load chat data
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
