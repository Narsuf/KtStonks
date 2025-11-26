package org.n27.ktstonks.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.n27.ktstonks.models.AlphaStock

class AlphaVantageClient(private val client: HttpClient, private val apiKey: String) {

    suspend fun getStock(symbol: String): AlphaStock {
        val url = "https://www.alphavantage.co/query?function=OVERVIEW&symbol=$symbol&apikey=$apiKey"
        return client.get(url).body()
    }
}
