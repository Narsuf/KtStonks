package org.n27.ktstonks.service

import org.n27.ktstonks.client.AlphaVantageClient
import org.n27.ktstonks.models.AlphaStock

class StockService(private val client: AlphaVantageClient) {

    suspend fun fetchStock(symbol: String): AlphaStock = client.getStock(symbol)
}
