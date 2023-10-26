package ru.pis.sdclient

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(
        savedInstanceState: Bundle?,
        rootKey: String?
    ) {
        addPreferencesFromResource(R.xml.preferences)

        findPreference<ListPreference>("languages_list")?.setOnPreferenceChangeListener { _, newValue ->
            setLocale(newValue.toString(), resources)
            activity?.recreate()
            switchToFragment(null, requireActivity(), SettingsFragment())
            true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = object : OnBackPressedCallback(
            true
        ) {
            override fun handleOnBackPressed() {}
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }
}
