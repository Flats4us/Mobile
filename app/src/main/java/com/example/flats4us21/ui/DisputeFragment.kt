import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.flats4us21.DrawerActivity
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
        setupMenu()
    }

    private fun setupMenu() {
        val moreMenu = binding.menuMore // Bind to the ImageView
        moreMenu.setOnClickListener { showPopupMenu(it) }
    }

    private fun showPopupMenu(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(R.menu.toolbar_menu, popup.menu)
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

    private fun setupViewPager() {
        val viewPager: ViewPager2 = binding.viewPager
        val tabLayout: TabLayout = binding.tabs

        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 2

            override fun createFragment(position: Int): Fragment {
                val isClosedDispute = position == 1 // Zamknięte spory dla pozycji 1, otwarte dla 0
                return SampleDisputeContentFragment(this@DisputeFragment, isClosedDispute)
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
            (activity as? DrawerActivity)!!.replaceFragment(chatFragment)
            //replace(R.id.fragment_container_disputes1, chatFragment)
            addToBackStack(null)
            commit()
        }
    }

}

class SampleDisputeContentFragment(
    private val disputeClickListener: OnDisputeClickListener,
    private val isClosedDispute: Boolean
) : Fragment() {

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
            layoutManager = LinearLayoutManager(context)
            adapter = DisputeAdapter(getSampleDisputes(), disputeClickListener)
        }
    }

    private fun getSampleDisputes(): List<Dispute> {
        return if (isClosedDispute) {
            // Zwróć listę zamkniętych sporów
            listOf(Dispute("Zamknięty Spór 1", "Opis zamkniętego sporu 1"), Dispute("Zamknięty Spór 2", "Opis zamkniętego sporu 2"))
        } else {
            // Zwróć listę otwartych sporów
            listOf(Dispute("Otwarty Spór 1", "Opis otwartego sporu 1"), Dispute("Otwarty Spór 2", "Opis otwartego sporu 2"))
        }
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
