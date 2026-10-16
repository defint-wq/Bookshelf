package com.example.bookshelf.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.bookshelf.BookshelfApplication
import com.example.bookshelf.BuildConfig
import com.example.bookshelf.data.BooksRepository
import com.example.bookshelf.network.Book

sealed interface BookshelfUiState {
    data class Success(val books: List<Book>) : BookshelfUiState
    object Error : BookshelfUiState
    object Loading : BookshelfUiState
}

class BookshelfViewModel(private val booksRepository: BooksRepository) : ViewModel() {
    var bookshelfUiState: BookshelfUiState by mutableStateOf(BookshelfUiState.Loading)
        private set

    init {
        getBooks()
    }

    fun getBooks(query: String = "jazz history") {
        viewModelScope.launch {
            bookshelfUiState = BookshelfUiState.Loading
            Log.d("Bookshelf", "key length=${BuildConfig.BOOKS_API_KEY.length}")
            bookshelfUiState = try {
                val response = booksRepository.getBooks(query)
                BookshelfUiState.Success(response.items)
            } catch (e: IOException) {
                Log.e("Bookshelf", "IOException: ${e.message}", e)
                BookshelfUiState.Error
            } catch (e: HttpException) {
                val body = e.response()?.errorBody()?.string()
                Log.e("Bookshelf", "HttpException ${e.code()} body=$body")
                BookshelfUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BookshelfApplication)
                val booksRepository = application.container.booksRepository
                BookshelfViewModel(booksRepository = booksRepository)
            }
        }
    }
}