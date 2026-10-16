package com.example.bookshelf.data

import com.example.bookshelf.network.Amphibian
import com.example.bookshelf.network.AmphibiansApiService

interface AmphibiansRepository {
    suspend fun getAmphibians(): List<Amphibian>
}

class NetworkAmphibiansRepository(
    private val amphibiansApiService: AmphibiansApiService
): AmphibiansRepository {
    override suspend fun getAmphibians(): List<Amphibian> = amphibiansApiService.getAmphibians()
}