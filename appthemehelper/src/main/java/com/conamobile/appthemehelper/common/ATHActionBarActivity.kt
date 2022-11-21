package com.conamobile.appthemehelper.common

import androidx.appcompat.widget.Toolbar
import com.conamobile.appthemehelper.util.ToolbarContentTintHelper

class ATHActionBarActivity : ATHToolbarActivity() {

    override fun getATHToolbar(): Toolbar? {
        return ToolbarContentTintHelper.getSupportActionBarView(supportActionBar)
    }
}
