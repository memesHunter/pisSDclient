package ru.pis.sdclient

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun switchToFragment(bundle: Bundle?, activity: FragmentActivity, fragment: Fragment) {
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