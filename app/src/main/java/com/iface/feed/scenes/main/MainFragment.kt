package com.iface.feed.scenes.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.iface.feed.utilities.EventObserver
import com.iface.feed.databinding.MainFragmentBinding
import com.iface.feed.utilities.viewmodelfactory.getViewModelFactory
import com.iface.feed.scenes.main.dialog.InsertURLDialog
import com.iface.feed.utilities.binding.PlayerViewAdapter.playIndexThenPausePreviousPlayer
import com.iface.feed.utilities.callback.RecyclerViewScrollListener

class MainFragment : Fragment() {

    private val viewModel by viewModels<MainViewModel> { getViewModelFactory() }
    private lateinit var dataBinding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = MainFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = this@MainFragment.viewModel
        }
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        dataBinding.recyclerView.addOnScrollListener(object: RecyclerViewScrollListener() {
            override fun onItemIsFirstVisibleItem(index: Int) {
                // play just visible item
                if (index != -1)
                    playIndexThenPausePreviousPlayer(index)
            }

        })

        viewModel.showInsertURLLiveData.observe(viewLifecycleOwner, EventObserver {
            InsertURLDialog.newInstance { posts ->
                viewModel.addPosts(posts)
                dataBinding.recyclerView.smoothScrollToPosition(0)
            }.show(childFragmentManager, "")
        })
    }

}