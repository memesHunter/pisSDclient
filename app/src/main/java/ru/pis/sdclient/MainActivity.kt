package ru.pis.sdclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private var lastFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up navigation controls
        bottomNav = findViewById(R.id.bottom_nav_view)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_connect -> {
                    switchFragment(ConnectFragment())
                    true
                }
                R.id.action_history -> {
                    switchFragment(HistoryFragment())
                    true
                }
//                R.id.action_settings -> {
//                    switchFragment(SettingsFragment())
//                    true
//                }
                else -> false
            }
        }

        // Set the default fragment
        switchFragment(ConnectFragment())
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}

