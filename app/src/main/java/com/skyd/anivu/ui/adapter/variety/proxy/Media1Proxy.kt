package com.skyd.anivu.ui.adapter.variety.proxy


import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import com.skyd.anivu.R
import com.skyd.anivu.databinding.ItemMedia1Binding
import com.skyd.anivu.ext.fileSize
import com.skyd.anivu.ext.toDateTimeString
import com.skyd.anivu.ext.tryAddIcon
import com.skyd.anivu.model.bean.VideoBean
import com.skyd.anivu.ui.adapter.variety.Media1ViewHolder
import com.skyd.anivu.ui.adapter.variety.VarietyAdapter


class Media1Proxy(
    private val adapter: VarietyAdapter,
    private val onPlay: (VideoBean) -> Unit,
    private val onOpenDir: (VideoBean) -> Unit,
    private val onRemove: (VideoBean) -> Unit,
) : VarietyAdapter.Proxy<VideoBean, ItemMedia1Binding, Media1ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Media1ViewHolder {
        val holder = Media1ViewHolder(
            ItemMedia1Binding.inflate(LayoutInflater.from(parent.context), parent, false),
        )
        holder.binding.apply {
            btnMedia1Options.setOnClickListener { v ->
                val popup = PopupMenu(v.context, v)
                popup.menuInflater.inflate(R.menu.menu_media_item, popup.menu)

                popup.setOnMenuItemClickListener { menuItem: MenuItem ->
                    val data = adapter.dataList.getOrNull(holder.bindingAdapterPosition)
                    if (data !is VideoBean) return@setOnMenuItemClickListener false
                    when (menuItem.itemId) {
                        R.id.action_media_item_remove -> {
                            onRemove(data)
                            true
                        }

                        else -> false
                    }
                }
                popup.menu.tryAddIcon(v.context)
                popup.show()
            }
        }
        holder.itemView.setOnClickListener {
            val data = adapter.dataList.getOrNull(holder.bindingAdapterPosition)
            if (data !is VideoBean) return@setOnClickListener
            if (data.isDir) {
                onOpenDir(data)
            } else if (data.isMedia(parent.context)) {
                onPlay(data)
            }
        }
        return holder
    }

    override fun onBindViewHolder(
        holder: Media1ViewHolder,
        data: VideoBean,
        index: Int,
        action: ((Any?) -> Unit)?
    ) {
        val context = holder.itemView.context
        holder.binding.apply {
            tvMedia1Title.text = data.name
            tvMedia1Date.text = data.date.toDateTimeString()
            tvMedia1Size.text = data.size.fileSize(context)
            ivMedia1IsVideo.isVisible = data.isMedia(context)
        }
    }
}