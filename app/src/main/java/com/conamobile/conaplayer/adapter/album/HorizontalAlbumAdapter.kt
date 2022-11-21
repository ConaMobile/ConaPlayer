package com.conamobile.conaplayer.adapter.album

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.conamobile.conaplayer.glide.GlideApp
import com.conamobile.conaplayer.glide.RetroGlideExtension
import com.conamobile.conaplayer.glide.RetroMusicColoredTarget
import com.conamobile.conaplayer.helper.HorizontalAdapterHelper
import com.conamobile.conaplayer.interfaces.IAlbumClickListener
import com.conamobile.conaplayer.model.Album
import com.conamobile.conaplayer.util.MusicUtil
import com.conamobile.conaplayer.util.color.MediaNotificationProcessor

class HorizontalAlbumAdapter(
    activity: FragmentActivity,
    dataSet: List<Album>,
    albumClickListener: IAlbumClickListener,
) : AlbumAdapter(
    activity, dataSet, HorizontalAdapterHelper.LAYOUT_RES, albumClickListener
) {

    override fun createViewHolder(view: View, viewType: Int): ViewHolder {
        val params = view.layoutParams as ViewGroup.MarginLayoutParams
        HorizontalAdapterHelper.applyMarginToLayoutParams(activity, params, viewType)
        return ViewHolder(view)
    }

    override fun setColors(color: MediaNotificationProcessor, holder: ViewHolder) {
        // holder.title?.setTextColor(ATHUtil.resolveColor(activity, android.R.attr.textColorPrimary))
        // holder.text?.setTextColor(ATHUtil.resolveColor(activity, android.R.attr.textColorSecondary))
    }

    override fun loadAlbumCover(album: Album, holder: ViewHolder) {
        if (holder.image == null) return
        GlideApp.with(activity).asBitmapPalette().albumCoverOptions(album.safeGetFirstSong())
            .load(RetroGlideExtension.getSongModel(album.safeGetFirstSong()))
            .into(object : RetroMusicColoredTarget(holder.image!!) {
                override fun onColorReady(colors: MediaNotificationProcessor) {
                    setColors(colors, holder)
                }
            })
    }

    override fun getAlbumText(album: Album): String {
        return MusicUtil.getYearString(album.year)
    }

    override fun getItemViewType(position: Int): Int {
        return HorizontalAdapterHelper.getItemViewType(position, itemCount)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    companion object {
        val TAG: String = AlbumAdapter::class.java.simpleName
    }
}
