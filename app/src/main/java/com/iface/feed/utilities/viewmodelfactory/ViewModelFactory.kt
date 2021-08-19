package com.iface.feed.utilities.viewmodelfactory
import android.content.ClipboardManager
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.iface.feed.scenes.main.MainViewModel
import com.iface.feed.scenes.main.dialog.InsertURLDialogViewModel

/**
 * Factory for all ViewModels.
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    val clipboardManager: ClipboardManager,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ) = with(modelClass) {
        when {
            isAssignableFrom(MainViewModel::class.java) ->
                MainViewModel()
            isAssignableFrom(InsertURLDialogViewModel::class.java) ->
                InsertURLDialogViewModel {
                    val itemCount = (clipboardManager.primaryClip?.itemCount ?: 0)
                    if (itemCount > 0) {
                        return@InsertURLDialogViewModel clipboardManager.primaryClip?.getItemAt(itemCount - 1)?.text.toString()
                    } else {
                        return@InsertURLDialogViewModel ""
                    }
                }
//            isAssignableFrom(AddEditTaskViewModel::class.java) ->
//                AddEditTaskViewModel(tasksRepository)
//            isAssignableFrom(TasksViewModel::class.java) ->
//                TasksViewModel(tasksRepository, handle)
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T
}
