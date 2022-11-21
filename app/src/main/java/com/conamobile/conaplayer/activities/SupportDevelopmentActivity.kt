package com.conamobile.conaplayer.activities

import android.os.Bundle
import android.view.MenuItem
import com.conamobile.appthemehelper.util.ToolbarContentTintHelper
import com.conamobile.conaplayer.activities.base.AbsThemeActivity
import com.conamobile.conaplayer.databinding.ActivityDonationBinding
import com.conamobile.conaplayer.extensions.openUrl
import com.conamobile.conaplayer.extensions.setStatusBarColorAuto
import com.conamobile.conaplayer.extensions.setTaskDescriptionColorAuto
import com.conamobile.conaplayer.extensions.surfaceColor


class SupportDevelopmentActivity : AbsThemeActivity() {

    lateinit var binding: ActivityDonationBinding
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBarColorAuto()
        setTaskDescriptionColorAuto()

        setupToolbar()

        binding.paypal.setOnClickListener {
            openUrl(PAYPAL_URL)
        }
        binding.kofi.setOnClickListener {
            openUrl(KOFI_URL)
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setBackgroundColor(surfaceColor())
        ToolbarContentTintHelper.colorBackButton(binding.toolbar)
        setSupportActionBar(binding.toolbar)
    }

    companion object {
        const val PAYPAL_URL = "https://paypal.me/"
        const val KOFI_URL = "https://ko-fi.com/"
    }
}
