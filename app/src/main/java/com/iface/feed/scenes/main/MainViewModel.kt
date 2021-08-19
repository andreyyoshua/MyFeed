package com.iface.feed.scenes.main

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.iface.feed.BR
import com.iface.feed.R
import com.iface.feed.scenes.main.post.PostViewModel
import com.iface.feed.utilities.Event
import com.iface.feed.utilities.binding.PlayerViewAdapter.releaseRecycledPlayers
import me.tatarka.bindingcollectionadapter2.*
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

class MainViewModel : ViewModel() {

    val items = ObservableArrayList<Any>()
    val itemBinding = OnItemBindClass<Any>().apply {
        // Map for empty state
        map(Unit::class.java, BR.item, R.layout.main_empty_state_row)

        // Map for posts
        map(PostViewModel::class.java, OnItemBind { itemBinding, position, item ->
            itemBinding.set(BR.item, R.layout.main_post_row)
                .bindExtra(BR.image_callback, item)
                .bindExtra(BR.video_callback, item)
            (items[position] as? PostViewModel)?.index?.value = position
        })
    }

    val adapter = object: BindingRecyclerViewAdapter<Any>() {
        override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
            super.onViewRecycled(holder)
            val position = holder.bindingAdapterPosition

            // We need to set loading to false because recycled view need to reload
            if (items.size > position && position >= 0) {
                (items[position] as? PostViewModel)?.finishLoadingImage?.value = false
                (items[position] as? PostViewModel)?.finishLoadingPlayer?.value = false
            }
            releaseRecycledPlayers(position)
        }
    }

    private val _showInsertURLLiveData = MutableLiveData<Event<Unit>>()
    val showInsertURLLiveData: LiveData<Event<Unit>> = _showInsertURLLiveData

    init {
        // Insert empty state as first state
        items.add(Unit)
    }

    fun clickAdd() {
        _showInsertURLLiveData.value = Event(Unit)
    }

    fun addPosts(posts: List<String>) {
        items.remove(Unit)
        if (posts.isEmpty()) return
        posts.forEachIndexed { index, element ->
            // Autoplay is true when it is the first and one and only post
            items.add(0, PostViewModel(element, items.isEmpty() && index == 0))
        }
    }
}