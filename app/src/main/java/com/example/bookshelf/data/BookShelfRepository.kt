package com.example.bookshelf.data

import com.example.bookshelf.network.BookResponse
import com.example.bookshelf.network.BooksApiService

interface BooksRepository {
    suspend fun getBooks(query: String): BookResponse
}

class NetworkBooksRepository(
    private val booksApiService: BooksApiService
): BooksRepository {
    override suspend fun getBooks(query: String): BookResponse = booksApiService.getBooks(query)
}