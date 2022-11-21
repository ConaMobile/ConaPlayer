package com.conamobile.conaplayer.fragments.settings

import android.os.Bundle
import android.view.View
import com.conamobile.appthemehelper.common.prefs.supportv7.ATEListPreference
import com.conamobile.conaplayer.HOME_ALBUM_GRID_STYLE
import com.conamobile.conaplayer.HOME_ARTIST_GRID_STYLE
import com.conamobile.conaplayer.R
import com.conamobile.conaplayer.TAB_TEXT_MODE

class PersonalizeSettingsFragment : AbsSettingsFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_ui)
    }

    override fun invalidateSettings() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeArtistStyle: ATEListPreference? = findPreference(HOME_ARTIST_GRID_STYLE)
        homeArtistStyle?.setOnPreferenceChangeListener { preference, newValue ->
            setSummary(preference, newValue)
            true
        }
        val homeAlbumStyle: ATEListPreference? = findPreference(HOME_ALBUM_GRID_STYLE)
        homeAlbumStyle?.setOnPreferenceChangeListener { preference, newValue ->
            setSummary(preference, newValue)
            true
        }
        val tabTextMode: ATEListPreference? = findPreference(TAB_TEXT_MODE)
        tabTextMode?.setOnPreferenceChangeListener { prefs, newValue ->
            setSummary(prefs, newValue)
            true
        }
    }
}
