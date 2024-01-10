package com.example.flats4us21.ui

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.R
import com.example.flats4us21.databinding.FragmentLoginBinding
import com.example.flats4us21.data.dto.LoginRequest
import com.example.flats4us21.data.dto.LoginResponse
import com.example.flats4us21.services.UserService
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Socket

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val userService: UserService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5166/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(UserService::class.java)
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dodaj listener do przycisku pokazującego/ukrywającego hasło
        val passwordToggle = binding.layoutPassword.findViewById<ImageButton>(R.id.passwordToggle)
        val passwordEditText = binding.layoutPassword.findViewById<EditText>(R.id.textPassword)
        passwordToggle.setOnClickListener {
            setPasswordVisibility(passwordToggle, passwordEditText)
        }

        // Dodaj listener do przycisku logowania
        binding.buttonLogin.setOnClickListener {
            val email = binding.layoutEmail.findViewById<EditText>(R.id.textEmail).text.toString()
            val password = passwordEditText.text.toString()
            login(email, password)
        }
    }

    private fun setPasswordVisibility(toggle: ImageButton, password: EditText) {
        if (password.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            toggle.setImageResource(R.drawable.baseline_visibility_24)
            password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            toggle.setImageResource(R.drawable.baseline_visibility_off_24)
            password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
    }

    private fun login(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)
        coroutineScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    userService.login(loginRequest)
                }
                handleLoginResponse(response)
            } catch (e: Exception) {
                // Obsługa wyjątku, np. wyświetlenie komunikatu o braku połączenia
                Log.e("LoginFragment", "Login error: ${e.message}")
                showToast("Error: Unable to connect to the server. Please check your internet connection.")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun handleLoginResponse(response: Response<LoginResponse>) {
        if (response.isSuccessful) {
            response.body()?.let { loginResponse ->
                // Logowanie danych użytkownika
                Log.d("LoginFragment", "Login successful: Token: ${loginResponse.token}")

                val email = binding.layoutEmail.findViewById<EditText>(R.id.textEmail).text.toString()
                val editor = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE).edit()
                editor.putString("token", loginResponse.token)
                editor.putString("email", email)  // Storing email
                editor.apply()

                (activity as? DrawerActivity)?.updateEmailInDrawer(email)
                val fragment = SearchFragment()
                (activity as? DrawerActivity)?.replaceFragment(fragment)


            }
        } else {
            // Możesz również dodać logowanie błędów w przypadku niepowodzenia
            Log.e("LoginFragment", "Login failed: ${response.errorBody()?.string()}")
            val errorMessage = getString(R.string.login_failed_message)
            binding.textViewErrorMessage.text = errorMessage
            binding.textViewErrorMessage.visibility = View.VISIBLE
        }
    }


        override fun onDestroyView() {
        super.onDestroyView()
        coroutineScope.cancel()
        _binding = null
    }



}
