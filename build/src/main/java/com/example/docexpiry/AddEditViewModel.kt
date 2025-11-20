package com.example.docexpiry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.docexpiry.data.CardDao

// Minimal ViewModel factory for Add/Edit if needed for future expansion
class AddEditViewModel(private val dao: CardDao) : ViewModel() {
    class Factory(private val dao: CardDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AddEditViewModel(dao) as T
        }
    }
}

