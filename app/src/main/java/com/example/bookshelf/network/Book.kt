package com.example.bookshelf.network

import kotlinx.serialization.Serializable

@Serializable
data class BookResponse(
    val items: List<Book> = emptyList()
)

@Serializable
data class Book(
    val id: String,
    val volumeInfo: VolumeInfo? = null
)

@Serializable
data class VolumeInfo(
    val title: String? = null,
    val imageLinks: ImageLinks? = null
)

@Serializable
data class ImageLinks(
    val thumbnail: String? = null
)