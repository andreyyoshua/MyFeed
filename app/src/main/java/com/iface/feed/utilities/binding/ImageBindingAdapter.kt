package com.iface.feed.utilities.binding

import android.graphics.drawable.Drawable
import android.webkit.WebSettings
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.iface.feed.utilities.callback.ImageStateCallback

object ImageBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["url", "callback"], requireAll = false)
    fun ImageView.loadImage(url: String?, callback: ImageStateCallback?) {
        if (!url.isNullOrEmpty()) {
            val glideUrl = GlideUrl(url, LazyHeaders.Builder().addHeader("User-Agent", WebSettings.getDefaultUserAgent(context)).build())
            Glide.with(this.context)
                .load(glideUrl)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        callback?.onImageFailLoaded()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        callback?.onImageSuccessLoad()
                        return false
                    }

                })
                .into(this)
        }
    }
}