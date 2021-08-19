package com.iface.feed.utilities.callback
import com.google.android.exoplayer2.Player

interface PlayerStateCallback {

    fun onIsPlayingChanged(isPlaying: Boolean, player: Player)
    /**
     * Callback to when the [PlayerView] has fetched the duration of video
     **/
    fun onVideoDurationRetrieved(duration: Long, player: Player)

    fun onVideoBuffering(player: Player)

    fun onStartedPlaying(player: Player)

    fun onFinishedPlaying(player: Player)

    fun onVideoLoadError(player: Player)
}