package ru.pis.sdclient

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

var lastFragment: Fragment? = null
lateinit var sharedPreferences: SharedPreferences

fun savePrefString(key: String, value: String) {
    sharedPreferences.edit().putString(key, value).apply()
}
fun switchToFragment(bundle: Bundle?, activity: FragmentActivity, fragment: Fragment) {
    // Set last visited Fragment
    lastFragment = fragment

    // Pass bundle to the new Fragment
    fragment.arguments = bundle

    // Get FragmentManager and start transaction
    val fragmentManager = activity.supportFragmentManager
    val fragmentTransaction = fragmentManager.beginTransaction()

    // Replace current fragment with Fragment
    fragmentTransaction.replace(R.id.fragment_container, fragment)

    // Add transaction to the back stack and commit
    fragmentTransaction.addToBackStack(null)
    fragmentTransaction.commit()
}

fun changeNavBarTitle(activity: FragmentActivity, itemID: Int, title: String) {
    (activity as AppCompatActivity).findViewById<BottomNavigationView>(R.id.bottom_nav_view).menu.findItem(itemID).title = title
}