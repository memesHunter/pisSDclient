package ru.pis.sdclient

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defaultPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        setLocale(defaultPreferences.getString("languages_list", "en") ?: "en", resources)

        setContentView(R.layout.activity_main)

        sharedPreferences.edit().remove("session").apply()

        // Set up navigation controls
        bottomNav = findViewById(R.id.bottom_nav_view)
        bottomNav.selectedItemId = R.id.action_connect
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_connect -> {
                    if (sharedPreferences.getString("session", "") != "") {
                        navBarSwitchFragment(SDMenuFragment())
                    } else {
                        navBarSwitchFragment(ConnectFragment())
                    }

                }
                R.id.action_history -> {
                    navBarSwitchFragment(HistoryFragment())
                }
                R.id.action_settings -> {
                    navBarSwitchFragment(SettingsFragment())
                }
                else -> false
            }
        }

        // Set the default fragment
        navBarSwitchFragment(lastFragment ?: ConnectFragment())
    }

    private fun navBarSwitchFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        return true
    }
}

