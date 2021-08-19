package com.iface.feed.scenes.main.post

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.Player
import com.iface.feed.scenes.main.post.usecase.PostUseCase
import com.iface.feed.scenes.main.post.usecase.PostUseCaseInterface
import com.iface.feed.utilities.callback.ImageStateCallback
import com.iface.feed.utilities.callback.PlayerStateCallback

class PostViewModel(inputtedUrl: String, autoPlay: Boolean = false, useCase: PostUseCaseInterface = PostUseCase()): ImageStateCallback,
    PlayerStateCallback {
    val title = MutableLiveData<String>()
    val autoPlay = MutableLiveData<Boolean>().apply { value = autoPlay }
    val url = MutableLiveData<String>()
    val index = MutableLiveData<Int>()
    val showPlayer = MediatorLiveData<Boolean>()
    val showImage = MediatorLiveData<Boolean>()
    val finishLoadingPlayer = MediatorLiveData<Boolean>()
    val finishLoadingImage = MediatorLiveData<Boolean>()

    init {
        useCase.checkURLType(inputtedUrl) { contentType ->
            val image = contentType.startsWith("image/")
            val video = contentType.startsWith("video/")
            showPlayer.postValue(video)
            showImage.postValue(image)
            url.postValue(inputtedUrl)
            title.postValue(if (image) "Downloading Image" else if (video) "Downloading Video" else "Downloading Data")
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean, player: Player) {
        print("onIsPlayingChanged")
    }

    override fun onVideoDurationRetrieved(duration: Long, player: Player) {
        print("onVideoDurationRetrieved")
        finishLoadingPlayer.value = duration >= 0
    }

    override fun onVideoBuffering(player: Player) {
        print("onVideoBuffering")
    }

    override fun onStartedPlaying(player: Player) {
        print("onStartedPlaying")
    }

    override fun onFinishedPlaying(player: Player) {
        print("onFinishedPlaying")
    }

    override fun onVideoLoadError(player: Player) {
        print("onVideoLoadError")
        finishLoadingPlayer.value = false
    }

    override fun onImageSuccessLoad() {
        finishLoadingImage.value = true
    }

    override fun onImageFailLoaded() {
        finishLoadingImage.value = false
    }
}