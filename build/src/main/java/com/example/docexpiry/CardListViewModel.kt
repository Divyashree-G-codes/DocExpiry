package com.example.docexpiry

import androidx.lifecycle.*
import androidx.lifecycle.viewModelScope
import com.example.docexpiry.data.Card
import com.example.docexpiry.data.CardDao
import kotlinx.coroutines.launch

// ViewModel managing card list and operations
class CardListViewModel(private val dao: CardDao) : ViewModel() {
    val cards: LiveData<List<Card>> = dao.getAll()

    // coroutine-friendly delete
    fun delete(card: Card) = viewModelScope.launch { dao.delete(card) }

    class Factory(private val dao: CardDao) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CardListViewModel(dao) as T
        }
    }
}
