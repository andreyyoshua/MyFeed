package com.iface.feed.utilities.viewmodelfactory

import android.content.ClipboardManager
import android.content.Context
import androidx.fragment.app.Fragment

fun Fragment.getViewModelFactory(): ViewModelFactory {
//    val repository = (requireContext().applicationContext as TodoApplication).taskRepository
    return ViewModelFactory(activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager, this)
}
