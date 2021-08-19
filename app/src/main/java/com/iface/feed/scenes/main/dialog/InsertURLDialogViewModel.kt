package com.iface.feed.scenes.main.dialog

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.*
import com.iface.feed.R
import com.iface.feed.BR
import com.iface.feed.scenes.main.dialog.row.InsertURLViewModel
import com.iface.feed.utilities.Event
import me.tatarka.bindingcollectionadapter2.ItemBinding

class InsertURLDialogViewModel(val getLatestClipboard: (() -> String)) : ViewModel() {
    val items = ObservableArrayList<InsertURLViewModel>()
    val itemBinding = ItemBinding.of<InsertURLViewModel>(BR.item, R.layout.insert_url_row)

    var clipBoard = MediatorLiveData<String>()

    private val _closeLiveData = MutableLiveData<Event<Unit>>()
    val closeLiveData = _closeLiveData as LiveData<Event<Unit>>

    private val _postLiveData = MutableLiveData<Event<List<String>>>()
    val postLiveData = _postLiveData as LiveData<Event<List<String>>>

    val showAddMoreButton = MediatorLiveData<Boolean>().apply { value = false }
    val addPostButtonEnabled = MutableLiveData<Boolean>().apply { value = false }
    val addPostButtonTitle = MediatorLiveData<String>().apply { value = "Add Post" }

    init {
        // Default url View Model
        addUrl()
    }

    fun addPost() {
        _postLiveData.value = Event(items.filter { it.url.value?.isNotEmpty() == true }.map { it.url.value ?: "" })
    }

    fun addUrl() {
        val viewModel = InsertURLViewModel(this::removeURL)

        // Make add more button only appear after first index is filled
        showAddMoreButton.addSource(viewModel.url.map { it.isNotEmpty() }) {
            // Ensure only validate first index
            if (items.indexOf(viewModel) != 0) return@addSource
            showAddMoreButton.value = it
        }

        items.add(viewModel)
        if (items.size > 1) {
            items.forEachIndexed { index, insertURLViewModel ->
                insertURLViewModel.showCloseButton.value = index < items.size - 1
            }
        } else if (items.size == 1) {
            items.forEach { it.showCloseButton.value = false }
        }
        // Pass new clipboard to all items
        items.forEach { it.onClipBoardChanged(getLatestClipboard()) }

        // More button only appear if item size is not only one and below 10
        showAddMoreButton.value = items.size in 2..9

        // Change post button enabled and title based on url entered
        // Post button is enabled only if at least one url is filled in the field
        // Post button title is also adjusted here
        addPostButtonTitle.addSource(viewModel.url) {

            addPostButtonEnabled.value = items.map { it.url.value?.isEmpty() == true }.contains(false)

            // Ensure only validate first index
            if (items.indexOf(viewModel) != 0) return@addSource
            addPostButtonTitle.value = if (it.isEmpty()) "Add Post" else "Post all URL"
        }
    }

    private fun removeURL(viewModel: InsertURLViewModel) {
        items.remove(viewModel)
        showAddMoreButton.value = items.size in 2..9
        if (items.size > 1) {
            items.forEachIndexed { index, insertURLViewModel ->
                insertURLViewModel.showCloseButton.value = index < items.size - 1
            }
        } else if (items.size == 1) {
            items.forEach { it.showCloseButton.value = false }
            addPostButtonTitle.value = if (items[0].url.value.isNullOrEmpty()) "Add Post" else "Post all URL"
        }

        addPostButtonEnabled.value = items.map { it.url.value.isNullOrEmpty() }.contains(false)
    }

    fun closeDialog() {
        _closeLiveData.value = Event(Unit)
    }

    fun onClipBoardChanged(value: String) {
        items.forEach { it.onClipBoardChanged(value) }
        clipBoard.value = value
    }
}