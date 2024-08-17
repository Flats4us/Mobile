package com.example.flats4us21.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.example.flats4us21.DataStoreManager
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.R
import com.example.flats4us21.URL
import com.example.flats4us21.adapters.ImageSliderAdapter
import com.example.flats4us21.adapters.PaymentAdapter
import com.example.flats4us21.adapters.ProfileAdapter
import com.example.flats4us21.data.Rent
import com.example.flats4us21.databinding.FragmentRentDetailBinding
import com.example.flats4us21.viewmodels.RealEstateViewModel
import com.example.flats4us21.viewmodels.RentViewModel
import com.example.flats4us21.viewmodels.UserViewModel

private const val TAG = "RentDetailFragment"
class RentDetailFragment : Fragment() {
    private var _binding : FragmentRentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : RentViewModel
    private lateinit var propertyViewModel : RealEstateViewModel
    private lateinit var userViewModel : UserViewModel
    private var fetchedRent : Rent? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rentId = arguments?.getInt(RENT_ID, -1)

        viewModel = ViewModelProvider(requireActivity())[RentViewModel::class.java]
        propertyViewModel = ViewModelProvider(requireActivity())[RealEstateViewModel::class.java]
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        viewModel.getRent(rentId!!)

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE
            binding.detailLayout.visibility = if(isLoading) View.GONE else View.VISIBLE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                val resourceId = requireContext().resources.getIdentifier(errorMessage, "string", requireContext().packageName)
                val message = if (resourceId != 0) {
                    requireContext().getString(resourceId)
                } else {
                    errorMessage
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                viewModel.clearErrorMessage()
            }
        }

        viewModel.rent.observe(viewLifecycleOwner) { rent ->
            fetchedRent = rent
            bindOfferData(rent)
        }

        binding.fab.setOnClickListener {
            if (fetchedRent != null) {
                showDialog(fetchedRent!!)
            }
        }

    }

    private fun bindOfferData(rent: Rent?) {
        rent ?: return

        val imageSlider = binding.image
        imageSlider.adapter = ImageSliderAdapter(rent.propertyImages)
        val imageCount = imageSlider.adapter?.itemCount ?: 0
        var imageText = "1/$imageCount"
        binding.imageCount.text = imageText
        imageSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val currentImage = position + 1
                imageText = "$currentImage/$imageCount"
                binding.imageCount.text = imageText
            }
        })

        val url = "$URL/${rent.owner.profilePicture?.path}"
        Log.i(TAG, url)
        binding.ownerPhoto.load(url) {
            error(R.drawable.baseline_person_24)
        }
        binding.owner.text = getString(R.string.name_and_surname, rent.owner.name, rent.owner.surname)
        binding.ownerLayout.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(USER_ID, rent.owner.id)
            val fragment = ProfileFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }

        binding.startDate.text = rent.startDate.split("T")[0]
        binding.endDate.text = rent.startDate.split("T")[0]
        binding.duration.text = rent.duration.toString()
        binding.address.text = rent.propertyAddress

        val adapter = ProfileAdapter(
            rent.tenants,
            { position ->
                val userId = rent.tenants[position].userId
                Log.i(TAG, "userId: $userId")
                val fragment: Fragment = if(userId != userViewModel.myProfile.value!!.userId) {
                    val bundle = Bundle()
                    bundle.putInt(USER_ID, userId)
                    val fragment = ProfileFragment()
                    fragment.arguments = bundle
                    fragment
                } else {
                    MyProfileFragment()
                }
                (activity as? DrawerActivity)?.replaceFragment(fragment)
            },
            { position ->
                val userId = rent.tenants[position].userId
                Log.i(TAG, "userId: $userId")
                val bundle = Bundle()
                bundle.putInt(USER_ID, userId)
                val fragment = ReviewSubmissionFragment()
                fragment.arguments = bundle
                if (userId == userViewModel.myProfile.value!!.userId){
                    Toast.makeText(requireContext(), getString(R.string.opinion_about_myself), Toast.LENGTH_LONG).show()
                } else {
                    checkIfOpinionAboutUserExists(userId) { exists ->
                        if (!exists) {
                            (activity as? DrawerActivity)?.replaceFragment(fragment)
                        } else {
                            Toast.makeText(requireContext(), getString(R.string.already_submitted_opinion), Toast.LENGTH_LONG).show()
                        }
                    }
                }
            },
            rent.isFinished
        )

        binding.rentRecyclerView.adapter = adapter
        binding.rentRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val paymentAdapter = PaymentAdapter(rent.payments)

        binding.paymentRecyclerView.adapter = paymentAdapter
        binding.paymentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showDialog(rent: Rent) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_menu_rent_layout)

        val addArgumentButton = dialog.findViewById<View>(R.id.addArgumentButton)
        val addOpinionButton = dialog.findViewById<View>(R.id.addOpinionButton)
        val relatedOfferButton = dialog.findViewById<View>(R.id.relatedOfferButton)
        val addMeetingButton = dialog.findViewById<View>(R.id.meetButton)

        checkIfOpinionAboutRentExists(rent.rentId) { exists ->
            if (!exists) {
                addOpinionButton.visibility = View.VISIBLE
            } else {
                addOpinionButton.visibility = View.GONE
            }
        }

        if(rent.isFinished && DataStoreManager.userRole.value == "Student" && userViewModel.myProfile.value!!.userId == rent.mainTenantId) {
            addOpinionButton.visibility = View.VISIBLE
        } else {
            addOpinionButton.visibility = View.GONE
        }
        if(rent.isFinished) {
            addArgumentButton.visibility = View.GONE
            addMeetingButton.visibility = View.GONE
        } else {
            addArgumentButton.visibility = View.VISIBLE
            addMeetingButton.visibility = View.VISIBLE
        }

        addArgumentButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(RENT_ID, rent.rentId)
            val fragment = AddArgumentFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)?.replaceFragment(fragment)
            dialog.dismiss()
        }
        addOpinionButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(RENT_ID, rent.rentId)
            val fragment = AddPropertyOpinionFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)?.replaceFragment(fragment)
            dialog.dismiss()
        }
        relatedOfferButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(OFFER_ID, rent.offerId)
            val fragment = OfferDetailFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)?.replaceFragment(fragment)
            dialog.dismiss()
        }
        addMeetingButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(RENT_ID, rent.rentId)
            bundle.putInt(OFFER_ID, rent.offerId)
            val fragment = AddMeetingFragment()
            fragment.arguments = bundle
            (activity as? DrawerActivity)?.replaceFragment(fragment)
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

    private fun checkIfOpinionAboutUserExists(userId: Int, callback: (Boolean) -> Unit) {
        userViewModel.getProfile(userId)
        userViewModel.profile.observeOnce(viewLifecycleOwner) { profile ->
            if (profile != null) {
                if (profile.userId == userViewModel.myProfile.value!!.userId) {
                    callback(true)
                }
                if (!profile.userOpinions.isNullOrEmpty()) {
                    val opinions = profile.userOpinions
                    val myOpinion = opinions.firstOrNull { it.sourceUserId == userViewModel.myProfile.value!!.userId }
                    if (myOpinion != null) {
                        callback(true)
                    } else {
                        callback(false)
                    }
                } else {
                    callback(false)
                }
            }
        }
    }

    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(value: T) {
                observer.onChanged(value)
                removeObserver(this)
            }
        })
    }

    private fun checkIfOpinionAboutRentExists(propertyId: Int, callback: (Boolean) -> Unit) {
        propertyViewModel.getProperty(propertyId)
        propertyViewModel.property.observeOnce(viewLifecycleOwner) { property ->
            if (property != null) {
                if (property.rentOpinions.isNullOrEmpty()) {
                    callback(false)
                }
                if (!property.rentOpinions.isNullOrEmpty()) {
                    val opinions = property.rentOpinions
                    val myOpinion = opinions!!.firstOrNull { it.sourceUserId == userViewModel.myProfile.value!!.userId }
                    if (myOpinion != null) {
                        callback(true)
                    } else {
                        callback(false)
                    }
                } else {
                    callback(false)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}