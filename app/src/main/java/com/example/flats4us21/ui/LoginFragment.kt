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
import android.widget.Toast
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import com.example.flats4us21.DrawerActivity
import com.example.flats4us21.R
import com.example.flats4us21.databinding.FragmentLoginBinding
import com.example.flats4us21.data.dto.LoginRequest
import com.example.flats4us21.data.dto.LoginResponse
import com.example.flats4us21.interceptors.AuthInterceptor
import com.example.flats4us21.services.UserService
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val userService: UserService by lazy {

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(requireContext()))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5166/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
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

        val passwordToggle = binding.layoutPassword.findViewById<ImageButton>(R.id.passwordToggle)
        val passwordEditText = binding.layoutPassword.findViewById<EditText>(R.id.textPassword)
        passwordToggle.setOnClickListener {
            setPasswordVisibility(passwordToggle, passwordEditText)
        }

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
                Log.d("LoginFragment", "Login successful: Token: ${loginResponse.token}")

                val email = binding.layoutEmail.findViewById<EditText>(R.id.textEmail).text.toString()
                coroutineScope.launch {
                    saveToDataStore("token", loginResponse.token)
                    saveToDataStore("email", email)
                }

                (activity as? DrawerActivity)?.updateEmailInDrawer(email)
                val fragment = SearchFragment()
                (activity as? DrawerActivity)?.replaceFragment(fragment)
            }
        } else {
            Log.e("LoginFragment", "Login failed: ${response.errorBody()?.string()}")
            val errorMessage = getString(R.string.login_failed_message)
            binding.textViewErrorMessage.text = errorMessage
            binding.textViewErrorMessage.visibility = View.VISIBLE
        }
    }

    private suspend fun saveToDataStore(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        requireContext().dataStore.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coroutineScope.cancel()
        _binding = null
    }
}
