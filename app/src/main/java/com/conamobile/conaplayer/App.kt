package com.conamobile.conaplayer

import android.app.Application
import androidx.preference.PreferenceManager
import androidx.viewbinding.BuildConfig
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.conamobile.appthemehelper.ThemeStore
import com.conamobile.appthemehelper.util.VersionUtils
import com.conamobile.conaplayer.activities.ErrorActivity
import com.conamobile.conaplayer.activities.MainActivity
import com.conamobile.conaplayer.appshortcuts.DynamicShortcutManager
import com.conamobile.conaplayer.billing.BillingManager
import com.conamobile.conaplayer.helper.WallpaperAccentManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    lateinit var billingManager: BillingManager
    private val wallpaperAccentManager = WallpaperAccentManager(this)

    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin {
            androidContext(this@App)
            modules(appModules)
        }
        // default theme
        if (!ThemeStore.isConfigured(this, 3)) {
            ThemeStore.editTheme(this)
                .accentColorRes(com.conamobile.appthemehelper.R.color.md_deep_purple_A200)
                .coloredNavigationBar(true)
                .commit()
        }
        wallpaperAccentManager.init()

        if (VersionUtils.hasNougatMR())
            DynamicShortcutManager(this).initDynamicShortcuts()

        billingManager = BillingManager(this)

        // setting Error activity
        CaocConfig.Builder.create().errorActivity(ErrorActivity::class.java)
            .restartActivity(MainActivity::class.java).apply()

        // Set Default values for now playing preferences
        // This will reduce startup time for now playing settings fragment as Preference listener of AbsSlidingMusicPanelActivity won't be called
        PreferenceManager.setDefaultValues(this, R.xml.pref_now_playing_screen, false)
    }

    override fun onTerminate() {
        super.onTerminate()
        billingManager.release()
        wallpaperAccentManager.release()
    }

    companion object {
        private var instance: App? = null

        fun getContext(): App {
            return instance!!
        }

        fun isProVersion(): Boolean {
            return BuildConfig.DEBUG || instance?.billingManager!!.isProVersion
        }
    }
}
