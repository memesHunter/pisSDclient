package ru.pis.sdclient

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Init SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        sharedPreferences.edit().remove("address").apply()

        // Set up navigation controls
        bottomNav = findViewById(R.id.bottom_nav_view)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_connect -> {
                    if (sharedPreferences.contains("session")) {
                        navBarSwitchFragment(SDMenuFragment())
                    } else {
                        navBarSwitchFragment(ConnectFragment())
                    }

                }
                R.id.action_history -> {
                    navBarSwitchFragment(HistoryFragment())
                }
//                R.id.action_settings -> {
//                    switchFragment(SettingsFragment())
//                    true
//                }
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

