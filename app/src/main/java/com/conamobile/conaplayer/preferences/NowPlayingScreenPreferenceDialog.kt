package com.conamobile.conaplayer.preferences

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat.SRC_IN
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.conamobile.appthemehelper.common.prefs.supportv7.ATEDialogPreference
import com.conamobile.conaplayer.App
import com.conamobile.conaplayer.R
import com.conamobile.conaplayer.databinding.PreferenceNowPlayingScreenItemBinding
import com.conamobile.conaplayer.extensions.*
import com.conamobile.conaplayer.fragments.NowPlayingScreen
import com.conamobile.conaplayer.util.PreferenceUtil
import com.conamobile.conaplayer.util.ViewUtil
import com.conamobile.conaplayer.fragments.NowPlayingScreen.*

class NowPlayingScreenPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1,
    defStyleRes: Int = -1,
) : ATEDialogPreference(context, attrs, defStyleAttr, defStyleRes) {

    private val mLayoutRes = R.layout.preference_dialog_now_playing_screen

    override fun getDialogLayoutResource(): Int {
        return mLayoutRes
    }

    init {
        icon?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            context.colorControlNormal(),
            SRC_IN
        )
    }
}

class NowPlayingScreenPreferenceDialog : DialogFragment(), ViewPager.OnPageChangeListener {

    private var viewPagerPosition: Int = 0

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        this.viewPagerPosition = position
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater
            .inflate(R.layout.preference_dialog_now_playing_screen, null)
        val viewPager = view.findViewById<ViewPager>(R.id.now_playing_screen_view_pager)
            ?: throw  IllegalStateException("Dialog view must contain a ViewPager with id 'now_playing_screen_view_pager'")
        viewPager.adapter = NowPlayingScreenAdapter(requireContext())
        viewPager.addOnPageChangeListener(this)
        viewPager.pageMargin = ViewUtil.convertDpToPixel(32f, resources).toInt()
        viewPager.currentItem = PreferenceUtil.nowPlayingScreen.ordinal

        return materialDialog(R.string.pref_title_now_playing_screen_appearance)
            .setCancelable(false)
            .setPositiveButton(R.string.set) { _, _ ->
                val nowPlayingScreen = values()[viewPagerPosition]
                if (isNowPlayingThemes(nowPlayingScreen)) {
                    val result =
                        "${getString(nowPlayingScreen.titleRes)} theme is Pro version feature."
                    showToast(result)
                    requireContext().goToProVersion()
                } else {
                    PreferenceUtil.nowPlayingScreen = nowPlayingScreen
                }
            }
            .setView(view)
            .create()
            .colorButtons()
    }

    companion object {
        fun newInstance(): NowPlayingScreenPreferenceDialog {
            return NowPlayingScreenPreferenceDialog()
        }
    }
}

private class NowPlayingScreenAdapter(private val context: Context) : PagerAdapter() {

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val nowPlayingScreen = values()[position]

        val inflater = LayoutInflater.from(context)
        val binding = PreferenceNowPlayingScreenItemBinding.inflate(inflater, collection, true)
        Glide.with(context).load(nowPlayingScreen.drawableResId).into(binding.image)
        binding.title.setText(nowPlayingScreen.titleRes)
        if (isNowPlayingThemes(nowPlayingScreen)) {
            binding.proText.show()
            binding.proText.setText(R.string.pro)
        } else {
            binding.proText.hide()
        }
        return binding.root
    }

    override fun destroyItem(
        collection: ViewGroup,
        position: Int,
        view: Any,
    ) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return values().size
    }

    override fun isViewFromObject(view: View, instance: Any): Boolean {
        return view === instance
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.getString(values()[position].titleRes)
    }
}

private fun isNowPlayingThemes(screen: NowPlayingScreen): Boolean {
    return (screen == Full || screen == Card || screen == NowPlayingScreen.Plain || screen == Blur || screen == Color || screen == NowPlayingScreen.Simple || screen == NowPlayingScreen.BlurCard || screen == Circle || screen == NowPlayingScreen.Adaptive) && !App.isProVersion()
}