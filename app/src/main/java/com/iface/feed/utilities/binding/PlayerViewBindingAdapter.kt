package com.iface.feed.utilities.binding

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.BindingAdapter
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.iface.feed.IFaceApp
import com.iface.feed.utilities.callback.PlayerStateCallback


// extension function for show toast
fun Context.toast(text: String){
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

object PlayerViewAdapter {
    // for hold all players generated
    private var playersMap: MutableMap<Int, SimpleExoPlayer>  = mutableMapOf()
    // for hold current player
    private var currentPlayingVideo: Pair<Int, SimpleExoPlayer>? = null
    fun releaseAllPlayers(){
        playersMap.map {
            it.value.release()
        }
    }

    // call when item recycled to improve performance
    fun releaseRecycledPlayers(index: Int){
        playersMap[index]?.release()
    }

    // call when scroll to pause any playing player
    fun pauseCurrentPlayingVideo(){
        if (currentPlayingVideo != null){
            currentPlayingVideo?.second?.playWhenReady = false
        }
    }

    fun playIndexThenPausePreviousPlayer(index: Int){
        if (playersMap.get(index) == null) return
        pauseCurrentPlayingVideo()
        playersMap.get(index)?.playWhenReady = true
        currentPlayingVideo = Pair(index, playersMap.get(index)!!)
    }

    /*
    *  url is a url of stream video
    *  progressbar for show when start buffering stream
    * thumbnail for show before video start
    * */
    @JvmStatic
    @BindingAdapter(value = ["video_url", "on_state_change", "progressbar", "thumbnail", "autoPlay"], requireAll = false)
    fun PlayerView.loadVideo(url: String?, callback: PlayerStateCallback?, progressbar: ProgressBar?, thumbnail: ImageView?, autoPlay: Boolean = false) {
        if (url == null) return
        // Release old player first
        this.player?.release()

        val httpDataSourceFactory: HttpDataSource.Factory = DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)
        var defaultDataSourceFactory: DefaultDataSourceFactory = DefaultDataSourceFactory(
            context, httpDataSourceFactory
        )
        val simpleCache: SimpleCache = IFaceApp.simpleCache
        val cacheDataSourceFactory: DataSource.Factory = CacheDataSource.Factory()
            .setCache(simpleCache)
            .setUpstreamDataSourceFactory(httpDataSourceFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
        val player = SimpleExoPlayer.Builder(context)
            .setMediaSourceFactory(DefaultMediaSourceFactory(cacheDataSourceFactory)).build()

        player.playWhenReady = autoPlay
        player.repeatMode = Player.REPEAT_MODE_ALL
        // When changing track, retain the latest frame instead of showing a black screen
        setKeepContentOnPlayerReset(true)
        // We'll show the controller, change to true if want controllers as pause and start
        this.useController = false
        // Provide url to load the video from here

        val videoUri = Uri.parse(url)
        val mediaItem = MediaItem.fromUri(videoUri)
        val mediaSource = ProgressiveMediaSource.Factory(cacheDataSourceFactory).createMediaSource(mediaItem)

        player.setMediaSource(mediaSource)
        player.prepare()

        player.addListener(object : Player.Listener {

            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                this@loadVideo.context.toast("Oops! Error occurred while playing media. ${error.message}")
                callback?.onVideoLoadError(player)
            }

            override fun onPlayerErrorChanged(error: PlaybackException?) {
                super.onPlayerErrorChanged(error)
                callback?.onVideoLoadError(player)
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                callback?.onIsPlayingChanged(isPlaying, player)
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == Player.STATE_BUFFERING){
                    callback?.onVideoBuffering(player)
                    // Buffering..
                    // set progress bar visible here
                    // set thumbnail visible
                    thumbnail?.visibility = View.VISIBLE
                    progressbar?.visibility = View.VISIBLE
                }

                if (playbackState == Player.STATE_READY){
                    // [PlayerView] has fetched the video duration so this is the block to hide the buffering progress bar
                    progressbar?.visibility = View.GONE
                    // set thumbnail gone
                    thumbnail?.visibility = View.GONE
                    callback?.onVideoDurationRetrieved(this@loadVideo.player!!.duration, player)
                }

                if (playbackState == Player.STATE_READY && player.playWhenReady){
                    // [PlayerView] has started playing/resumed the video
                    callback?.onStartedPlaying(player)
                }
            }
        })

        this.player = player
    }
    /*
    *  url is a url of stream video
    *  progressbar for show when start buffering stream
    * thumbnail for show before video start
    * */
    @JvmStatic
    @BindingAdapter("item_index")
    fun PlayerView.setIndex(item_index: Int? = null) {
        // add player with its index to map
        if (playersMap.containsKey(item_index))
            playersMap.remove(item_index)
        if (item_index != null && player is SimpleExoPlayer)
            playersMap[item_index] = this.player as SimpleExoPlayer
    }
}