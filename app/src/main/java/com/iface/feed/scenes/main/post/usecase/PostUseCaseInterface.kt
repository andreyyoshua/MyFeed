package com.iface.feed.scenes.main.post.usecase

import java.io.IOException
import java.net.URL
import java.net.URLConnection

interface PostUseCaseInterface {
    fun checkURLType(url: String, callback: (String) -> Unit)
}

class PostUseCase: PostUseCaseInterface {
    override fun checkURLType(url: String, callback: (String) -> Unit) {
        Thread {
            try {
                val connection: URLConnection = URL(url).openConnection()
                val contentType: String = connection.getHeaderField("Content-Type")
                callback.invoke(contentType)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }
}