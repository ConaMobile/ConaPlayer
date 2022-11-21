package com.conamobile.conaplayer.activities

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import com.conamobile.appthemehelper.util.ATHUtil.isWindowBackgroundDark
import com.conamobile.appthemehelper.util.ColorUtil.lightenColor
import com.conamobile.appthemehelper.util.ToolbarContentTintHelper
import com.conamobile.conaplayer.activities.base.AbsThemeActivity
import com.conamobile.conaplayer.databinding.ActivityLicenseBinding
import com.conamobile.conaplayer.extensions.accentColor
import com.conamobile.conaplayer.extensions.drawAboveSystemBars
import com.conamobile.conaplayer.extensions.surfaceColor
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

class LicenseActivity : AbsThemeActivity() {
    private lateinit var binding: ActivityLicenseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLicenseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        ToolbarContentTintHelper.colorBackButton(binding.toolbar)
        try {
            val buf = StringBuilder()
            val json = assets.open("license.html")
            BufferedReader(InputStreamReader(json, StandardCharsets.UTF_8)).use { br ->
                var str: String?
                while (br.readLine().also { str = it } != null) {
                    buf.append(str)
                }
            }

            // Inject color values for WebView body background and links
            val isDark = isWindowBackgroundDark(this)
            val backgroundColor = colorToCSS(
                surfaceColor(Color.parseColor(if (isDark) "#424242" else "#ffffff"))
            )
            val contentColor = colorToCSS(Color.parseColor(if (isDark) "#ffffff" else "#000000"))
            val changeLog = buf.toString()
                .replace(
                    "{style-placeholder}", String.format(
                        "body { background-color: %s; color: %s; }", backgroundColor, contentColor
                    )
                )
                .replace("{link-color}", colorToCSS(accentColor()))
                .replace(
                    "{link-color-active}",
                    colorToCSS(
                        lightenColor(accentColor())
                    )
                )
            binding.license.loadData(changeLog, "text/html", "UTF-8")
        } catch (e: Throwable) {
            binding.license.loadData(
                "<h1>Unable to load</h1><p>" + e.localizedMessage + "</p>", "text/html", "UTF-8"
            )
        }
        binding.license.drawAboveSystemBars()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun colorToCSS(color: Int): String {
        return String.format(
            "rgb(%d, %d, %d)",
            Color.red(color),
            Color.green(color),
            Color.blue(color)
        ) // on API 29, WebView doesn't load with hex colors
    }
}