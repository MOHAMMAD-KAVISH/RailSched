package com.example.railsched

import android.app.*
import android.content.*
import android.os.*
import android.provider.Settings
import android.view.*
import android.widget.EditText
import android.widget.Toast

import androidx.appcompat.widget.Toolbar

import androidx.appcompat.app.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.railsched.customview.TrainProgressBar
import com.example.railsched.utils.FileUtils
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var sharedPref: SharedPreferences
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var homeLayout: View
    private lateinit var fragmentContainer: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedPref = getSharedPreferences("rail_sched_prefs", Context.MODE_PRIVATE)
        applySavedPreferences()
        createNotificationChannel()
        scheduleRakeReminder()

        val trainProgressBar = findViewById<TrainProgressBar>(R.id.trainProgressBar)
        trainProgressBar.animateProgressTo(0.85f) // Set train progress to 75%


        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        toolbar = findViewById(R.id.toolbar)
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
        homeLayout = findViewById(R.id.homeLayout)
        fragmentContainer = findViewById(R.id.fragment_container)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.baseline_menu_24)
        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener(this)

        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Schedule"
                1 -> "Forecast"
                2 -> "Resources"
                else -> null
            }
        }.attach()

        showViewPager()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> showViewPager()
            R.id.nav_settings -> replaceFragment(SettingsFragment())
            R.id.nav_logout -> logoutUser()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showViewPager() {
        homeLayout.visibility = View.VISIBLE
        fragmentContainer.visibility = View.GONE
        toolbar.title = "RailSched"
        supportFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    private fun replaceFragment(fragment: Fragment) {
        homeLayout.visibility = View.GONE
        fragmentContainer.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
        toolbar.title = "Settings"
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else if (fragmentContainer.visibility == View.VISIBLE) {
            showViewPager()
        } else {
            super.onBackPressed()
        }
    }

    private fun logoutUser() {
        sharedPref.edit().clear().apply()
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun applySavedPreferences() {
        val isDarkMode = sharedPref.getBoolean("dark_mode_enabled", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        val language = sharedPref.getString("language", "English")
        val locale = when (language?.lowercase(Locale.getDefault())) {
            "hindi" -> Locale("hi")
            "spanish" -> Locale("es")
            else -> Locale("en")
        }

        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "rail_sched_notifications"
            val channelName = "RailSched Notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun scheduleRakeReminder() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, RakeReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val triggerTime = System.currentTimeMillis() + 10000

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                } else {
                    Toast.makeText(this, "Exact alarm permission needed.", Toast.LENGTH_SHORT).show()
                    val settingsIntent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    startActivity(settingsIntent)
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            Toast.makeText(this, "SecurityException: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    // 🔽 Inflate Menu to add Save Log icon
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    // 🔽 Handle Menu Clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save_log -> {
                showSaveLogDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // 🔽 Show Dialog to Save Log
    private fun showSaveLogDialog() {
        val input = EditText(this).apply {
            hint = "Enter train log..."
            setPadding(30, 20, 30, 50)
        }

        AlertDialog.Builder(this)
            .setTitle("Save Train Log")
            .setView(input)
            .setPositiveButton("Save") { _, _ ->
                val logData = input.text.toString().trim()
                if (logData.isNotEmpty()) {
                    val success = FileUtils.saveTrainLog(this, "Train Log - ${Date()}\n\n$logData\n")
                    if (success) {
                        Toast.makeText(this, "Log saved.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to save log.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Log is empty.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
