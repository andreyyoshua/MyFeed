package com.iface.feed.scenes.main.dialog

import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.iface.feed.utilities.EventObserver
import com.iface.feed.databinding.InsertUrlDialogFragmentBinding
import com.iface.feed.utilities.viewmodelfactory.getViewModelFactory
import java.util.*

class InsertURLDialog : DialogFragment() {

    companion object {
        fun newInstance(onPost: ((List<String>) -> Unit)) = InsertURLDialog().apply { _onPost = onPost }
    }

    private var _onPost: ((List<String>) -> Unit)? = null
    private val viewModel by viewModels<InsertURLDialogViewModel> { getViewModelFactory() }
    private lateinit var dataBinding: InsertUrlDialogFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = InsertUrlDialogFragmentBinding.inflate(inflater, container, true).apply { viewModel = this@InsertURLDialog.viewModel }
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWidthToMatchParent()
        setupBackgroundTransparent()
        dataBinding.lifecycleOwner = viewLifecycleOwner
        val clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.addPrimaryClipChangedListener {
            sendLatestClipboard(clipboard)
        }
        sendLatestClipboard(clipboard)

        viewModel.closeLiveData.observe(viewLifecycleOwner, EventObserver {
            dismiss()
        })

        viewModel.postLiveData.observe(viewLifecycleOwner, EventObserver {
            _onPost?.invoke(it)
            dismiss()
        })
    }

    private fun sendLatestClipboard(clipboard: ClipboardManager) {
        val itemCount = (clipboard.primaryClip?.itemCount ?: 0)
        if (itemCount > 0) {
            viewModel.onClipBoardChanged(clipboard.primaryClip?.getItemAt(itemCount - 1)?.text.toString())
        }
    }
    fun DialogFragment.setupWidthToMatchParent() {
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    fun DialogFragment.setupBackgroundTransparent() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}
