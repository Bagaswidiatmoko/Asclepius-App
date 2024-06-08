package com.dicoding.asclepius.view.history

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.dicoding.asclepius.database.History
import com.dicoding.asclepius.repository.HistoryRepository

class HistoryViewModel(application: Application): ViewModel(){
    private val mHistoryRepository: HistoryRepository = HistoryRepository(application)

    fun delete(history: History) {
        mHistoryRepository.delete(history)
    }

    fun getAllHistory(): LiveData<List<History>> {
        return mHistoryRepository.getAllHistory()
    }

    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return HistoryViewModel(
                    (application)
                ) as T
            }
        }
    }
}