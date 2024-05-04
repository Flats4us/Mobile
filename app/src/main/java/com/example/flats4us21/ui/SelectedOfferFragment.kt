package com.example.flats4us21.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.data.StudentsPayment
import com.example.flats4us21.databinding.FragmentSelectedOfferBinding
import com.example.flats4us21.viewmodels.PaymentViewModel
import java.time.LocalDate
import java.time.temporal.ChronoUnit

private const val TAG = "SelectedOfferFragment"
class SelectedOfferFragment : Fragment() {
    private var _binding : FragmentSelectedOfferBinding? = null
    private val binding get() = _binding!!
    private lateinit var paymentViewModel : PaymentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectedOfferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val paymentId = arguments?.getInt(PAYMENT_ID, -1)
        val paymentId = 1

        paymentViewModel = ViewModelProvider(requireActivity())[PaymentViewModel::class.java]

        paymentViewModel.getStudentsPayment(paymentId)

        paymentViewModel.payment.observe(viewLifecycleOwner) {payment ->
            bindPaymentData(payment)
        }

        paymentViewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            Log.i(TAG, "isLoading $isLoading")
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.detailsLayout.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        paymentViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        binding.payButton.setOnClickListener {
            val fragment = PaymentMethodFragment()
            (activity as? DrawerActivity)!!.replaceFragment(fragment)
        }
    }

    private fun bindPaymentData(payment: StudentsPayment?) {
        payment ?: return

        binding.city.text = payment.property.city
        binding.street.text = payment.property.street
        binding.size.text = payment.property.area.toString()
        binding.room.text = payment.property.numberOfRooms.toString()


        binding.toPay.text = payment.amount.toString()
        binding.period.text = payment.period
        binding.left.text  = ChronoUnit.DAYS.between(LocalDate.now(), payment.toDate).toString()
    }
}