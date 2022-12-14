@file:Suppress("UNUSED_PARAMETER", "unused")

package com.conamobile.conaplayer.extensions

import android.content.Context
import android.view.Menu
import androidx.fragment.app.FragmentActivity

fun Context.setUpMediaRouteButton(menu: Menu) {}

fun FragmentActivity.installLanguageAndRecreate(code: String) {
    recreate()
}

fun Context.goToProVersion() {}

fun Context.installSplitCompat() {}