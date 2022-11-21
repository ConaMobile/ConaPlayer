package com.conamobile.conaplayer.fragments.settings

import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import com.conamobile.appthemehelper.common.prefs.supportv7.ATEListPreference
import com.conamobile.conaplayer.LANGUAGE_NAME
import com.conamobile.conaplayer.LAST_ADDED_CUTOFF
import com.conamobile.conaplayer.R
import com.conamobile.conaplayer.extensions.installLanguageAndRecreate
import com.conamobile.conaplayer.fragments.LibraryViewModel
import com.conamobile.conaplayer.fragments.ReloadType
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class OtherSettingsFragment : AbsSettingsFragment() {
    private val libraryViewModel by sharedViewModel<LibraryViewModel>()

    override fun invalidateSettings() {
        val languagePreference: ATEListPreference? = findPreference(LANGUAGE_NAME)
        languagePreference?.setOnPreferenceChangeListener { _, _ ->
            restartActivity()
            return@setOnPreferenceChangeListener true
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_advanced)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preference: Preference? = findPreference(LAST_ADDED_CUTOFF)
        preference?.setOnPreferenceChangeListener { lastAdded, newValue ->
            setSummary(lastAdded, newValue)
            libraryViewModel.forceReload(ReloadType.HomeSections)
            true
        }
        val languagePreference: Preference? = findPreference(LANGUAGE_NAME)
        languagePreference?.setOnPreferenceChangeListener { prefs, newValue ->
            setSummary(prefs, newValue)
            requireActivity().installLanguageAndRecreate(newValue.toString())
            true
        }
    }
}
