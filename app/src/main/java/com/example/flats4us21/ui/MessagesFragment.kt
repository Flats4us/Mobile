
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.flats4us21.R
import com.example.flats4us21.databinding.FragmentMessageBinding
import com.example.flats4us21.databinding.FragmentSampleDisputeContentBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MessagesFragment : Fragment(), OnDisputeClickListener {

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMessageBinding.inflate(inflater, container, false)
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
                return SampleMessageContentFragment(this@MessagesFragment, isClosedDispute)
            }
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) "Wiadomości z właścicielem" else "Wiadomości z współlokatorami"
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDisputeClicked(dispute: Dispute) {
        TODO()
    }

    class SampleMessageContentFragment(
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
                adapter = DisputeAdapter(getSampleMessages(), disputeClickListener)
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

        private fun getSampleMessages(): List<Dispute> {
            return if (isClosedDispute) {
                // Zwróć listę zamkniętych sporów
                listOf(Dispute("Wiadmość z współlokatorem 1", "wiadomość 1"), Dispute("Wiadmość z współlokatorem 1", "wiadomość 2"))
            } else {
                // Zwróć listę otwartych sporów
                listOf(Dispute("Wiadomość z właścicielem 1", "wiadomość 1"), Dispute("Wiadomość z właścicielem 2", "wiadomość 2"))
            }
        }
        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }

}


