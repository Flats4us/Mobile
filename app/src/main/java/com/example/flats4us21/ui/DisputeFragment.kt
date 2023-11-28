import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.flats4us21.R
import com.example.flats4us21.databinding.FragmentDisputeBinding
import com.example.flats4us21.databinding.FragmentSampleDisputeContentBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.io.Serializable

class DisputeFragment : Fragment(), OnDisputeClickListener {

    private var _binding: FragmentDisputeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDisputeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
    }

    private fun setupViewPager() {
        val viewPager: ViewPager2 = binding.viewPager
        val tabLayout: TabLayout = binding.tabs

        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 2
            override fun createFragment(position: Int): Fragment {
                // Pass this as the listener to the fragment
                return SampleDisputeContentFragment(this@DisputeFragment)
            }
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) "Otwarte" else "Zamknięte"
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDisputeClicked(dispute: Dispute) {
        val chatFragment = ChatFragment.newInstance(dispute)

        // Start the fragment transaction
        childFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container_disputes1, chatFragment)
            addToBackStack(null)
            commit()
        }
    }



}

class SampleDisputeContentFragment(private val disputeClickListener: OnDisputeClickListener) : Fragment() {

    private var _binding: FragmentSampleDisputeContentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSampleDisputeContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context) // Ustawienie LinearLayoutManager
            adapter = DisputeAdapter(getSampleDisputes(), disputeClickListener)
        }
    }


    private fun getSampleDisputes(): List<Dispute> {
        return listOf(Dispute("Spor 1", "Opis sporu 1"), Dispute("Spor 2", "Opis sporu 2"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

interface OnDisputeClickListener {
    fun onDisputeClicked(dispute: Dispute)
}

class DisputeAdapter(private val disputes: List<Dispute>, private val listener: OnDisputeClickListener) : RecyclerView.Adapter<DisputeAdapter.DisputeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisputeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dispute, parent, false)
        return DisputeViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: DisputeViewHolder, position: Int) {
        val dispute = disputes[position]
        holder.bind(dispute)
    }

    override fun getItemCount(): Int = disputes.size

    class DisputeViewHolder(itemView: View, private val listener: OnDisputeClickListener) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)

        fun bind(dispute: Dispute) {
            titleTextView.text = dispute.title
            descriptionTextView.text = dispute.description

            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onDisputeClicked(dispute)
                }
            }
        }
    }
}

data class Dispute(val title: String, val description: String)  : Serializable
