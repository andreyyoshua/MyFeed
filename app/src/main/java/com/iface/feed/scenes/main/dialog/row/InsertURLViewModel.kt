package com.iface.feed.scenes.main.dialog.row

import android.webkit.URLUtil
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

class InsertURLViewModel(val onRemoved: ((InsertURLViewModel) -> Unit)) {
    val url = MutableLiveData<String>()
    val isValidURL = MediatorLiveData<Boolean>().apply { value = true }
    val clipBoard = MutableLiveData<String>()
    val showPasteClipBoardButton = MediatorLiveData<Boolean>()
    var showCloseButton = MutableLiveData<Boolean>()

    init {
        isValidURL.addSource(url) {
            if (it.length < 5) {
                isValidURL.value = true
                return@addSource
            }
            isValidURL.value = URLUtil.isValidUrl(it)
        }
        showPasteClipBoardButton.addSource(url) {
            showPasteClipBoardButton.value = it.isEmpty()
        }
    }

    fun pasteFromClipboard() {
        url.value = clipBoard.value
        showPasteClipBoardButton.value = false
    }

    fun onClipBoardChanged(value: String) {
        clipBoard.value = value
        showPasteClipBoardButton.value = value.isNotEmpty() && url.value.isNullOrEmpty()
    }

    fun remove() {
        onRemoved.invoke(this)
    }
}
