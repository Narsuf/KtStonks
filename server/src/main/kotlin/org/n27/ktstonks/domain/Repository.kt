package org.n27.ktstonks.domain

import org.n27.ktstonks.domain.model.Stock

interface Repository {

    suspend fun getStock(symbol: String): Result<Stock>
}
