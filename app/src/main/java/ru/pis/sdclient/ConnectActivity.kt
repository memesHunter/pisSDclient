package ru.pis.sdclient;

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ConnectActivity : AppCompatActivity() {

    private lateinit var addressEditText: EditText
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        addressEditText = findViewById(R.id.address_edit_text)
        bottomNavigationView = findViewById(R.id.bottom_nav_view)

        val connectButton: Button = findViewById(R.id.connect_button)
        connectButton.setOnClickListener {
            val address: String = addressEditText.text.toString()

            if (address == "127.0.0.1:7860") {

                // Login successful
                val intent = Intent(this, SDMenuActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Login failed
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_history -> {
                    val intent = Intent(this, HistoryActivity::class.java)
                    startActivity(intent)
                    true
                }
//                R.id.action_settings -> {
//                    val intent = Intent(this, SettingsActivity::class.java)
//                    startActivity(intent)
//                    true
//                }
                else -> false
            }
        }

    }
}
