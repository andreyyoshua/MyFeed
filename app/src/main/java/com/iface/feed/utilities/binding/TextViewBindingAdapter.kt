package com.iface.feed.utilities.binding

import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.io.IOException
import java.net.URL
import java.net.URLConnection

object TextViewBindingAdapter {
    @JvmStatic
    @BindingAdapter("url")
    fun TextView.showLoadingType(url: String?) {
        if (!url.isNullOrEmpty()) {
            Thread {
                try {
                    val connection: URLConnection = URL(url).openConnection()
                    val contentType: String = connection.getHeaderField("Content-Type")
                    val image = contentType.startsWith("image/") //true if image
                    val video = contentType.startsWith("video/")
                    post {
                        text = if (image) "Downloading Image" else if (video) "Downloading Video" else "Downloading Data"
                    }
                    Log.i("IS IMAGE", "" + image + " " + contentType)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.start()
        }
    }
}