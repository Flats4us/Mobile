package com.example.flats4us

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.flats4us.data.MyProfile
import com.example.flats4us.data.utils.Constants.Companion.USER_ID
import com.example.flats4us.ui.ArgumentsFragment
import com.example.flats4us.ui.CalendarFragment
import com.example.flats4us.ui.ChatsFragment
import com.example.flats4us.ui.MapFragment
import com.example.flats4us.ui.MyProfileFragment
import com.example.flats4us.ui.MyRentsFragment
import com.example.flats4us.ui.NoInternetFragment
import com.example.flats4us.ui.NotificationsFragment
import com.example.flats4us.ui.OwnerOffersFragment
import com.example.flats4us.ui.OwnerPropertiesFragment
import com.example.flats4us.ui.RoommatesFragment
import com.example.flats4us.ui.SearchFragment
import com.example.flats4us.ui.SettingsFragment
import com.example.flats4us.ui.StartScreenFragment
import com.example.flats4us.ui.TechnicalProblemFragment
import com.example.flats4us.ui.UserOpinionsFragment
import com.example.flats4us.ui.WatchedOffersListFragment
import com.example.flats4us.viewmodels.NotificationViewModel
import com.example.flats4us.viewmodels.UserViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant

const val URL = "http://172.21.40.120:5166"
//const val URL = "http://172.27.80.1:5166"
private const val TAG = "DrawerActivity"
class DrawerActivity : AppCompatActivity(), NetworkChangeReceiver.NetworkChangeListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var headerView: View
    private lateinit var userRole: String
    private lateinit var viewModel: UserViewModel
    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var networkChangeReceiver: NetworkChangeReceiver

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "notification_channel"
            val channelName = "com.example.flats4us"
            val channelDescription = "Channel for app notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        networkChangeReceiver = NetworkChangeReceiver(this)
        registerReceiver(networkChangeReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        notificationViewModel = ViewModelProvider(this)[NotificationViewModel::class.java]
        DataStoreManager.initialize(applicationContext)
        DataStoreManager.tokenExpiresAt.observe(this) { expiresAt ->
            if (expiresAt != null && isTokenExpired(expiresAt)) {
                logout()
            }
        }

        setContentView(R.layout.activity_drawer)
        val toolbar : Toolbar = findViewById(R.id.toolbar)
        drawerLayout  = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view)
        headerView = navView.getHeaderView(0)
        val notificationButton : ImageButton = findViewById(R.id.notification_button)
        notificationButton.setOnClickListener {
            replaceFragment(NotificationsFragment())
            drawerLayout.closeDrawers()
        }
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getDataFromDataStore()
        setupDrawerContent()

        DataStoreManager.userRole.observe(this) { user ->
            navView.menu.clear()
            userRole = user ?: ""
            setUserMenu(userRole)
        }

        viewModel.myProfile.observe(this) { myProfile ->
            Log.i(TAG, "myProfile: $myProfile")
            if(myProfile != null) {
                setMyProfile(myProfile)
            }
        }
        observeUnreadNotifications()

    }

    private fun observeUnreadNotifications() {
        notificationViewModel.unreadNotifications.observe(this) { notifications ->
            Log.d(TAG, "Notification observer: $notifications")
            if (notifications != null) {
                val unreadNotificationsCount = findViewById<TextView>(R.id.unreadNotificationCount)
                unreadNotificationsCount.text = notifications.size.toString()
                unreadNotificationsCount.isVisible = notifications.isNotEmpty()
            }
        }
    }

    private fun setUserMenu(userRole: String) {
        val notificationLayout : ConstraintLayout = findViewById(R.id.notificationLayout)
        when (userRole) {
            "Student" -> {
                navView.inflateMenu(R.menu.student_nav_menu)
                notificationLayout.isVisible = true
            }
            "Owner" -> {
                navView.inflateMenu(R.menu.owner_nav_menu)
                notificationLayout.isVisible = true
            }
            else -> {
                navView.inflateMenu(R.menu.guest_nav_menu)
                notificationLayout.isVisible = false
            }
        }
    }

    private fun isTokenExpired(expiresAt: Long): Boolean {
        val currentTime = Instant.now().epochSecond
        return currentTime > expiresAt
    }

    fun setMyProfile(profile: MyProfile) {
        notificationViewModel = ViewModelProvider(this)[NotificationViewModel::class.java]
        observeUnreadNotifications()
        val profilePicture = headerView.findViewById<ImageView>(R.id.profilePicture)
        val mail : TextView = headerView.findViewById(R.id.mail)
        val nameAndSurname : TextView = headerView.findViewById(R.id.nameAndSurname)

        Log.i(TAG, "Is not null $profile")
        mail.isVisible = true
        mail.text = profile.email
        nameAndSurname.text = getString(R.string.name_and_surname, profile.name, profile.surname)
        val url = "$URL/${profile.profilePicture?.path ?: ""}"
        profilePicture.load(url) {
            error(R.drawable.baseline_person_24)
        }
        profilePicture.isVisible = true
        notificationViewModel.getUnreadNotifications()
        notificationViewModel.startConnection()
    }

    private fun setupDrawerContent() {
        navView.setNavigationItemSelectedListener { menuItem ->
            selectDrawerItem(menuItem)
            true
        }
        supportFragmentManager.commit {
            add(R.id.frameLayout,StartScreenFragment(), null)
        }
    }

    private fun selectDrawerItem(menuItem: MenuItem) {
        when(menuItem.itemId){
            R.id.nav_start -> replaceFragment(SearchFragment())
            R.id.nav_map -> replaceFragment(MapFragment())
        }
        when (userRole) {
            "Student" -> {
                handleStudentAndOwnerMenuItems(menuItem)
                when (menuItem.itemId) {
                    R.id.nav_observed -> replaceFragment(WatchedOffersListFragment())
                    R.id.nav_roommates -> replaceFragment(RoommatesFragment())
                    R.id.reviews -> {
                        val bundle = Bundle()
                        bundle.putInt(USER_ID, viewModel.profile.value?.userId ?: -1)
                        val fragment = UserOpinionsFragment()
                        fragment.arguments = bundle
                        replaceFragment(fragment)
                    }

                }
            }
            "Owner" -> {
                handleStudentAndOwnerMenuItems(menuItem)
                when (menuItem.itemId) {
                    R.id.nav_my_properties -> replaceFragment(OwnerPropertiesFragment())
                    R.id.nav_my_offers -> replaceFragment(OwnerOffersFragment())
                }
            }
            else -> {
                when (menuItem.itemId) {
                    R.id.nav_login -> replaceFragment(StartScreenFragment())
                }
            }
        }
        drawerLayout.closeDrawers()
    }

    private fun handleStudentAndOwnerMenuItems(menuItem: MenuItem) {
        when(menuItem.itemId){
            R.id.nav_messages -> replaceFragment(ChatsFragment())
            R.id.nav_profile -> replaceFragment(MyProfileFragment())
            R.id.nav_settings -> replaceFragment(SettingsFragment())
            R.id.nav_my_rentals -> replaceFragment(MyRentsFragment())
            R.id.nav_conflicts -> replaceFragment(ArgumentsFragment())
            R.id.nav_calendar -> replaceFragment(CalendarFragment())
            R.id.nav_report_issue -> replaceFragment(TechnicalProblemFragment())
            R.id.nav_logout -> logout()
        }
    }

    fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        if(fragment != NoInternetFragment()) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()
    }

    fun goBack() {
        supportFragmentManager.popBackStack()
    }

    fun clearBackStack() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    private fun logout() {
        Log.i(TAG, "test")
        viewModel.logout {
            if(it) {
                notificationViewModel.stopConnection()
                viewModelStore.clear()
                viewModel = ViewModelProvider(this)[UserViewModel::class.java]
                notificationViewModel = ViewModelProvider(this)[NotificationViewModel::class.java]
                clearUserData()
                val profilePicture = headerView.findViewById<ImageView>(R.id.profilePicture)
                val mail : TextView = headerView.findViewById(R.id.mail)
                val nameAndSurname : TextView = headerView.findViewById(R.id.nameAndSurname)

                profilePicture.setImageResource(R.drawable.baseline_person_24)
                profilePicture.isVisible = false
                nameAndSurname.text = getString(R.string.guest)
                mail.visibility = View.INVISIBLE

                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

                replaceFragment(StartScreenFragment())
            }
        }
    }

    private fun clearUserData() {
        lifecycleScope.launch {
            DataStoreManager.clearUserData()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()
        if(DataStoreManager.userRole.value != null) {
            notificationViewModel.stopConnection()
        }
    }

    override fun onResume() {
        super.onResume()
        if(DataStoreManager.userRole.value != null) {
            notificationViewModel.startConnection()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkChangeReceiver)
        if(DataStoreManager.userRole.value != null) {
            notificationViewModel.stopConnection()
        }
    }

    override fun onNetworkChanged(isConnected: Boolean) {
        if (!isConnected) {
            replaceFragment(NoInternetFragment())
        }
    }

    private fun getDataFromDataStore() {
        viewModel.checkIfDataStoreIsNotEmpty { loginResponseExists ->
            if (loginResponseExists) {
                viewModel.getMyProfile {
                    if(it) {
                        val profile = viewModel.myProfile.value
                        if (profile != null) {
                            setMyProfile(profile)
                            runBlocking {
                                DataStoreManager.saveUserData(viewModel.loginResponse.value!!)
                                replaceFragment(SearchFragment())
                            }
                        }
                    }
                }
            } else {
                replaceFragment(StartScreenFragment())
            }
        }
    }


}